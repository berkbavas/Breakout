package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.HashSet;
import java.util.Set;

public class TickProcessor {
    private final GameObjects gameObjects;

    public TickProcessor(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
    }

    public TickProcessorResult process(Set<Collision> potentialCollisions, double deltaTime) {
        Ball ball = gameObjects.getBall();

        if (potentialCollisions.isEmpty()) {
            return processNoCollision(ball, deltaTime);
        }

        final double timeToCollision = findMinimumTimeAmongGivenPotentialCollisions(potentialCollisions);
        final boolean collides = timeToCollision <= deltaTime;
        // Find collisions among all potential collisions that will take place in given deltaTime.
        final Set<Collision> collisions = filterCollisionsUnderGivenTime(potentialCollisions, timeToCollision);

        if (!collides) {
            // If we are here a collision won't happen because timeToCollision > deltaTime.
            TickProcessorResult result = processNoCollision(ball, deltaTime);
            result.addCollisions(collisions);
            return result;
        }

        // If we are here a collision will happen because timeToCollision <= deltaTime.
        return processCollision(ball, collisions, timeToCollision);
    }

    private static TickProcessorResult processCollision(Ball ball, Set<Collision> collisions, double timeToCollision) {
        Vector2D collisionNormal = calculateCollisionNormal(collisions);

        Ball newBall = ball.collide(collisionNormal, timeToCollision, 2 * Util.EPSILON);
        TickProcessorResult result = new TickProcessorResult(newBall, timeToCollision + 2 * Util.EPSILON, collisionNormal);
        result.addCollisions(collisions);

        return result;
    }

    private static TickProcessorResult processNoCollision(Ball ball, double deltaTime) {
        Ball newBall = ball.move(deltaTime);
        return new TickProcessorResult(newBall, deltaTime);
    }

    private static Vector2D calculateCollisionNormal(Set<Collision> collisions) {
        Vector2D collisionNormal = new Vector2D(0, 0);

        for (Collision collision : collisions) {
            StaticNode node = collision.getCollider();
            LineSegment2D edge = collision.getEdge();

            if (node instanceof World) {
                collisionNormal = collisionNormal.add(edge.getNormal(LineSegment2D.NormalOrientation.INWARDS));
            } else if (node instanceof Paddle || node instanceof Brick) {
                collisionNormal = collisionNormal.add(edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS));
            } else {
                throw new RuntimeException("This branch is not implemented!");
            }
        }

        return collisionNormal.normalized();
    }

    private static Set<Collision> filterCollisionsUnderGivenTime(Set<Collision> collisions, double timeToCollision) {
        Set<Collision> filteredCollisions = new HashSet<>();
        for (Collision collision : collisions) {
            if (Util.isFuzzyBetween(0.0, collision.getTimeToCollision(), timeToCollision)) {
                filteredCollisions.add(collision);
            }
        }
        return filteredCollisions;
    }

    private static double findMinimumTimeAmongGivenPotentialCollisions(Set<Collision> collisions) {
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
