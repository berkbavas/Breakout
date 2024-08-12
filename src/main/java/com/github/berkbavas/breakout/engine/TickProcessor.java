package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TickProcessor {

    public TickProcessorResult process(GameObjects objects, Set<Collision> collisions, double ifps) {
        Ball ball = objects.getBall();

        if (collisions.isEmpty()) {
            return processNoCollision(ball, ifps);
        }

        final double timeToCollision = findMinimumTimeAmongGivenCollisions(collisions);
        final boolean collides = timeToCollision <= ifps;
        final Set<Collision> earliestCollisions = filterCollisionsUnderGivenTime(collisions, timeToCollision);

        if (!collides) {
            // If we are here a collision won't happen because timeToCollision > ifps.
            TickProcessorResult result = processNoCollision(ball, ifps);
            result.addCollisions(earliestCollisions);
            return result;
        }


//        for (Collision collision : earliestCollisions) {
//            System.out.printf("[%.8f] [%d] Collision: %s \n", ifps, earliestCollisions.size(), collision);
//        }

        // If we are here a collision will happen because timeToCollision <= ifps.
        return processCollision(ball, earliestCollisions, timeToCollision, ifps);
    }

    private TickProcessorResult processCollision(Ball ball, Set<Collision> collisions, double timeToCollision, double ifps) {
        Vector2D collisionNormal = calculateCollisionNormal(collisions);
        Ball nextBall = ball.collide(collisionNormal, timeToCollision, 2 * Util.EPSILON);

        // Check if ball is still colliding
        for (Collision collision : collisions) {
            LineSegment2D edge = collision.getEdge();
            List<Point2D> intersections = nextBall.findIntersection(edge);

            // If there is one intersection point then, the edge is tangent to the ball and nothing to do.

            if (intersections.size() == 2) {
                // If this is the case, then translate the ball along the line passing through ball's center
                // and the midpoint of intersections.

                Point2D i0 = intersections.get(0);
                Point2D i1 = intersections.get(1);

                Point2D midpoint = i0.add(i1).multiply(0.5);
                Vector2D translationDirection = midpoint.subtract(nextBall.getCenter()).toVector2D().normalized();
                double translationDistance = (nextBall.getRadius() - midpoint.distanceTo(nextBall.getCenter())) + 2 * Util.EPSILON;

                // We hope after this translation the ball will not collide with the edge anymore.
                nextBall = nextBall.translateAlongDirection(translationDirection, translationDistance);

            }
        }

        TickProcessorResult result = new TickProcessorResult(nextBall, true, collisionNormal);
        result.addCollisions(collisions);
        return result;
    }

    private TickProcessorResult processNoCollision(Ball ball, double ifps) {
        Ball nextBall = ball.move(ifps);
        return new TickProcessorResult(nextBall, false, null);
    }

    private Vector2D calculateCollisionNormal(Set<Collision> collisions) {
        Vector2D collisionNormal = new Vector2D(0, 0);

        for (Collision collision : collisions) {
            StaticNode node = collision.getCollider();
            LineSegment2D edge = collision.getEdge();

            if (node instanceof World) {
                collisionNormal = collisionNormal.add(edge.getNormal(LineSegment2D.NormalOrientation.INWARDS));
            } else if (node instanceof Brick) {
                throw new RuntimeException("This branch is not implemented!");
            } else if (node instanceof Paddle) {
                collisionNormal = collisionNormal.add(edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS));
            } else {
                throw new RuntimeException("This branch is not implemented!");
            }
        }

        return collisionNormal.normalized();
    }

    private Set<Collision> filterCollisionsUnderGivenTime(Set<Collision> collisions, double timeToCollision) {
        Set<Collision> filteredCollisions = new HashSet<>();
        for (Collision collision : collisions) {
            if (Util.isFuzzyBetween(0.0, collision.getTimeToCollision(), timeToCollision)) {
                filteredCollisions.add(collision);
            }
        }

        return filteredCollisions;
    }

    private double findMinimumTimeAmongGivenCollisions(Set<Collision> collisions) {
        double minTimeToCollision = Double.MAX_VALUE;

        // First find the minimum time among all collisions.
        for (Collision collision : collisions) {
            double timeToCollision = collision.getTimeToCollision();
            if (timeToCollision <= minTimeToCollision) {
                minTimeToCollision = timeToCollision;
            }
        }

        return minTimeToCollision;
    }
}
