package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;

import java.util.Optional;

public class CollisionConstructor {
    private final Vector2D velocity;
    private final double deltaTime;
    private final double speed;
    private final Point2D center;

    public CollisionConstructor(Circle circle, Vector2D velocity, double deltaTime) {
        this.velocity = velocity;
        this.deltaTime = deltaTime;
        this.speed = velocity.length();
        this.center = circle.getCenter();
    }

    public Optional<Collision> constructIfPossible(Collider collider, ColliderEdge edge, CriticalPointPair pair) {
        final boolean isProspectiveCollision = pair instanceof SeparateCriticalPointPair;

        if (isProspectiveCollision) {
            SeparateCriticalPointPair separate = (SeparateCriticalPointPair) pair;
            Point2D pointOnCircle = separate.getPointOnCircle();
            Point2D pointOnEdge = separate.getPointOnEdge();
            if (isPointWithinCollisionTrajectory(center, pointOnEdge, velocity)) {
                double distance = Point2D.distanceBetween(pointOnEdge, pointOnCircle);
                double timeToCollision = distance / speed;
                boolean isInevitableCollision = timeToCollision <= deltaTime;

                if (isInevitableCollision) {
                    return Optional.of(new InevitableCollision(collider, edge, separate, timeToCollision));
                } else {
                    return Optional.of(new PotentialCollision(collider, edge, separate, timeToCollision));
                }
            }

        } else {
            Vector2D normal = collider.getNormalOf(edge);
            boolean colliding = normal.dot(velocity) < -Util.EPSILON;

            // If the ball is colliding, this needs to be resolved, i.e., the velocity should be reflected
            // by PresentCollisionResolver so we count this case as PresentCollision.
            if (colliding) {
                return Optional.of(new PresentCollision(collider, edge, pair));
            }
        }

        return Optional.empty();
    }

    public static boolean isPointWithinCollisionTrajectory(Point2D pointOnCircle, Point2D test, Vector2D velocity) {
        Vector2D circleToTestPoint = test.subtract(pointOnCircle);
        double dot = Vector2D.dot(circleToTestPoint, velocity);
        return dot > 0;
    }
}
