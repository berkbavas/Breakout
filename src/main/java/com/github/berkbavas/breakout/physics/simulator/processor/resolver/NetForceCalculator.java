package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Ray2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.Conflict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class NetForceCalculator {
    private final Set<Collider> colliders;
    private Vector2D gravity = new Vector2D(0, Constants.Physics.GRAVITY.get());
    private double tolerance = Constants.Physics.NET_FORCE_CALCULATOR_TOLERANCE[0];
    private Ray2D rayFromCenterToGround;

    public NetForceCalculator(Set<Collider> colliders) {
        this.colliders = colliders;

        Constants.Physics.GRAVITY.addListener((o, newValue, oldValue) ->
                gravity = new Vector2D(0, newValue.floatValue()));
    }

    public Result process(Ball ball, double deltaTime) {
        var result = calculate(ball);

        var type = result.getType();
        Vector2D netForce = result.getNetForce();

        if (type == ResultType.APPLY_NET_FORCE) {
            ball.move(netForce, deltaTime);
        } else if (type == ResultType.BALL_IS_SLIDING) {
            ball.slide(netForce, deltaTime);
        } else if (type == ResultType.BALL_IS_AT_EQUILIBRIUM) {
            ball.move(deltaTime); // No acceleration, just move with the current velocity.
        } else if (type == ResultType.BALL_IS_AT_CORNER) {
            ball.move(netForce, deltaTime);
        }

        ball.setNetForce(netForce);

        return result;
    }

    public Result calculate(Ball ball) {
        tolerance = Constants.Physics.NET_FORCE_CALCULATOR_TOLERANCE[0];
        rayFromCenterToGround = new Ray2D(ball.getCenter(), gravity);

        var conflicts = CollisionEngine.findConflicts(colliders, ball.enlarge(tolerance));

        var gravitySubjects = conflicts.stream().filter(conflict -> gravity.dot(conflict.getNormal()) <= 0).collect(Collectors.toList());

        if (gravitySubjects.isEmpty()) {
            // No colliders around the ball, i.e., the ball is free to move.
            return new Result(ResultType.APPLY_NET_FORCE, gravity);
        } else if (gravitySubjects.size() == 1) {
            return resolveSingleGravitySubject(gravitySubjects.get(0), ball);
        } else {
            return resolveMultipleGravitySubjects(gravitySubjects, ball);
        }
    }

    private Result resolveSingleGravitySubject(Conflict conflict, Ball ball) {

        // Hmm... There is one collider around the ball.
        // Let's find out if the ball is sliding on the collider.

        // If velocity.normal is zero then it means that velocity is perpendicular to the normal,
        // and it is sliding on the collider.

        Vector2D velocity = ball.getVelocity();
        Vector2D normal = conflict.getNormal();

        // velocity.normal ~ 0 if the ball is sliding on the collider.
        final boolean sliding = Util.isFuzzyZero(velocity.dot(normal));

        if (sliding) {
            // If we are here then the ball is sliding on the collider.
            return createSlidingResult(conflict, ball);
        } else {

            if (ball.isStationary()) {
                // Ball is not moving.
                if (isBallAtCorner(conflict)) {
                    return createBallAtCornerResult(conflict, ball);
                } else {
                    return new Result(ResultType.BALL_IS_AT_EQUILIBRIUM, Vector2D.ZERO);
                }
            }

            Vector2D netForce = gravity.rejectionOf(normal);  // We need rejection, not projection
            return new Result(ResultType.APPLY_NET_FORCE, netForce);
        }
    }

    private Result resolveMultipleGravitySubjects(List<Conflict> conflicts, Ball ball) {

        if (isAtEquilibrium(conflicts, ball)) {
            // If there are more than one collider around ball, then the ball is at equilibrium.
            // If the ball is at equilibrium then the normals of the colliders around ball is against velocity,
            // hence they are "holding" the ball against the gravitation.
            return new Result(ResultType.BALL_IS_AT_EQUILIBRIUM, Vector2D.ZERO);
        }

        var cornerSubjects = new ArrayList<Conflict>();


        for (var conflict : conflicts) {
            if (isBallAtCorner(conflict)) {
                cornerSubjects.add(conflict);
            }
        }

        if (cornerSubjects.isEmpty()) {
            // If there are more than one collider around ball, then the ball is at equilibrium.
            // If the ball is at equilibrium then the normals of the colliders around ball is against velocity,
            // hence they are "holding" the ball against the gravitation.
            return new Result(ResultType.BALL_IS_AT_EQUILIBRIUM, Vector2D.ZERO);
        } else if (cornerSubjects.size() == 1) {
            // Ball is at corner.
            var conflict = cornerSubjects.get(0);
            return createBallAtCornerResult(conflict, ball);
        } else {

            if (isAtEquilibrium(cornerSubjects, ball)) {
                return new Result(ResultType.BALL_IS_AT_EQUILIBRIUM, Vector2D.ZERO);
            } else {
                // Ball is at corner.

                Point2D center = ball.getCenter();
                Vector2D ground = gravity.normal();

                var sorted = cornerSubjects.stream().sorted((c0, c1) -> {
                    var contact0 = c0.getContact();
                    var contact1 = c1.getContact();

                    Vector2D centerToContact0 = contact0.getPointOnEdge().subtract(center);
                    Vector2D centerToContact1 = contact1.getPointOnEdge().subtract(center);

                    Vector2D p0 = centerToContact0.projectOnto(ground);
                    Vector2D p1 = centerToContact1.projectOnto(ground);

                    return Double.compare(p0.length(), p1.length());
                }).collect(Collectors.toList());

                Conflict conflict = sorted.get(0);

                return createBallAtCornerResult(conflict, ball);
            }
        }
    }

    private Result createSlidingResult(Conflict conflict, Ball ball) {
        // Let's find push and resistance vectors.

        Vector2D velocity = ball.getVelocity();

        Collider collider = conflict.getCollider();
        Vector2D normal = conflict.getNormal();

        double frictionCoefficient = collider.getFrictionCoefficient();
        double resistanceMagnitude = gravity.projectOnto(normal).multiply(frictionCoefficient).length();

        Vector2D resistance = velocity.normalized().reversed().multiply(resistanceMagnitude);
        Vector2D rejection = gravity.rejectionOf(normal);  // We need rejection, not projection.
        Vector2D netForce = rejection.add(resistance);
        return new Result(ResultType.BALL_IS_SLIDING, netForce);
    }

    private Result createBallAtCornerResult(Conflict conflict, Ball ball) {
        Point2D pointOnEdge = conflict.getContact().getPointOnEdge();
        Vector2D circleToEdge = pointOnEdge.subtract(ball.getCenter());
        Vector2D projection = gravity.projectOnto(circleToEdge);
        Vector2D netForce = gravity.rejectionOf(circleToEdge).add(projection.reversed());
        return new Result(ResultType.BALL_IS_AT_CORNER, netForce);
    }

    private boolean isAtEquilibrium(List<Conflict> conflicts, Ball ball) {
        Point2D center = ball.getCenter();
        Vector2D ground = gravity.normal();

        for (var conflict : conflicts) {
            boolean ballIsOnCollider = rayFromCenterToGround.findIntersection(conflict.getEdge()).isPresent();
            if (ballIsOnCollider) {
                Vector2D rejection = gravity.rejectionOf(conflict.getNormal());
                if (Util.isFuzzyZero(rejection.l2norm())) {
                    return true;
                }
            }
        }

        for (int i = 0; i < conflicts.size(); ++i) {
            for (int j = i + 1; j < conflicts.size(); ++j) {
                var contact0 = conflicts.get(i).getContact();
                var contact1 = conflicts.get(j).getContact();

                Vector2D centerToContact0 = contact0.getPointOnEdge().subtract(center);
                Vector2D centerToContact1 = contact1.getPointOnEdge().subtract(center);

                Vector2D p0 = centerToContact0.projectOnto(ground);
                Vector2D p1 = centerToContact1.projectOnto(ground);

                if (p0.dot(p1) < 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isBallAtCorner(Conflict conflict) {
        return rayFromCenterToGround.findIntersection(conflict.getEdge()).isEmpty();
    }

    public enum ResultType {
        APPLY_NET_FORCE,
        BALL_IS_SLIDING,
        BALL_IS_AT_EQUILIBRIUM,
        BALL_IS_AT_CORNER
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class Result {
        private final ResultType type;
        private final Vector2D netForce;
    }

}
