package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

public class NetForceCalculator {
    private final Set<Collider> colliders;

    public NetForceCalculator(Set<Collider> colliders) {
        this.colliders = colliders;
    }

    public static void process(Result result, Ball ball, double deltaTime) {
        if (result.getType() == ResultType.FREE) {
            ball.move(deltaTime, result.getPull());
        } else if (result.getType() == ResultType.SLIDE) {
            ball.slide(deltaTime, result.getPull(), result.getResistance());
        }

        // For drawing
        ball.setPull(result.getPull());
        ball.setResistance(result.getResistance());
    }

    public Result process(Ball ball, double deltaTime) {
        var result = calculate(ball);
        process(result, ball, deltaTime);
        return result;
    }

    public Result calculate(Ball ball) {
        final Vector2D gravity = new Vector2D(0, Constants.Physics.GRAVITY[0]);
        final double tolerance = Constants.Physics.NET_FORCE_CALCULATOR_TOLERANCE[0];

        Vector2D velocity = ball.getVelocity();

        var conflicts = CollisionEngine.findConflicts(colliders, ball.enlarge(tolerance));

        var gravitySubjects = conflicts.stream()
                .filter(conflict -> gravity.dot(conflict.getNormal()) <= 0)
                .collect(Collectors.toList());

        if (gravitySubjects.isEmpty()) {
            // No colliders around the ball, i.e., the ball is free to move.
            return new Result(ResultType.FREE, gravity, Vector2D.ZERO);
        } else if (gravitySubjects.size() == 1) {
            // Hmm... There is one collider around the ball.
            // Let's find out if the ball is sliding on the collider.

            // If v.n is zero then it means that velocity is perpendicular to the normal,
            // and it is sliding on the collider.

            var frictionSubjects = gravitySubjects.stream()
                    .filter(conflict -> Util.isFuzzyZero(velocity.dot(conflict.getNormal())))
                    .collect(Collectors.toList());

            if (frictionSubjects.isEmpty()) {

                Vector2D normal = gravitySubjects.get(0).getNormal();
                Vector2D rejection = gravity.rejectionOf(normal);  // We need rejection, not projection.

                return new Result(ResultType.FREE, rejection, Vector2D.ZERO);
            } else {
                // If we are here then the ball is sliding on the collider.
                // Let's find push and resistance vectors.

                Collider collider = frictionSubjects.get(0).getCollider();
                Vector2D normal = frictionSubjects.get(0).getNormal();
                double frictionCoefficient = collider.getFrictionCoefficient();
                double resistanceMagnitude = gravity.projectOnto(normal).multiply(frictionCoefficient).length();
                Vector2D resistance = velocity.normalized().reversed().multiply(resistanceMagnitude);
                Vector2D rejection = gravity.rejectionOf(normal);  // We need rejection, not projection.

                return new Result(ResultType.SLIDE, rejection, resistance);
            }
        } else {
            // If there are more than one collider around ball, then the ball is at equilibrium.
            // Note that the normals of colliders around ball is against velocity.
            // In other words, they are "holding" the ball against gravity.
            return new Result(ResultType.NET_ZERO, Vector2D.ZERO, Vector2D.ZERO);
        }
    }

    public enum ResultType {
        NET_ZERO,
        SLIDE,
        FREE
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class Result {
        private final ResultType type;
        private final Vector2D pull;
        private final Vector2D resistance;
    }

}
