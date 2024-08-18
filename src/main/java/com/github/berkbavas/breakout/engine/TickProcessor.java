package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public final class TickProcessor {
    private final GameObjects gameObjects;

    public TickProcessor(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void preprocess() {
        Ball ball = gameObjects.getBall();
        World world = gameObjects.getWorld();

        Point2D center = ball.getCenter();
        double radius = ball.getRadius();
        double padding = radius + 2 * Util.EPSILON;
        double x = Util.clamp(world.getLeftTop().getX() + padding, center.getX(), world.getRightTop().getX() - padding);
        double y = Util.clamp(world.getLeftTop().getY() + padding, center.getY(), world.getLeftBottom().getY() - padding);

        Point2D clampedCenter = new Point2D(x, y);

        if (!center.equals(clampedCenter)) {
            // We need to update the position of the ball.
            Ball newBall = ball.constructFrom(clampedCenter);
            gameObjects.setBall(newBall);
        }
    }

    public void updatePaddle(Point2D newTopLeft) {
        gameObjects.updatePaddle(newTopLeft);
    }

    public TickResult process(double deltaTime) {
        final Set<Collision> potentialCollisions = CollisionDetector.findPotentialCollisions(gameObjects);

        final Pair<Set<Collision>, Double> earliestCollisionsAndTimeToCollision = findEarliestCollisions(potentialCollisions);
        final Set<Collision> earliestCollisions = earliestCollisionsAndTimeToCollision.getKey();
        final double timeToCollision = earliestCollisionsAndTimeToCollision.getValue();

        if (earliestCollisions.isEmpty()) {
            // We are good, there is no collision.
            gameObjects.moveBall(deltaTime);

            // Return an empty set as there is no collision.
            return new TickResult(false, deltaTime, Set.of());
        }

        final boolean collides = timeToCollision <= deltaTime;

        if (!collides) {
            // If we are here a collision won't happen in the given delta time.
            gameObjects.moveBall(deltaTime);
            return new TickResult(false, deltaTime, earliestCollisions);
        }

        // If we are here a collision will happen in the given delta time.

        // Calculate the collision normal
        Vector2D collisionNormal = calculateCollisionNormal(earliestCollisions);

        // Who is the collider?
        StaticNode collider = TickResult.findCollider(earliestCollisions);

        if (timeToCollision < 0.01) {
            gameObjects.collideBall(collider, collisionNormal, 0);
        } else {
            gameObjects.collideBall(collider, collisionNormal, timeToCollision);
        }

        // Process the ball
        return new TickResult(true, timeToCollision, earliestCollisions);
    }


    public static Vector2D calculateCollisionNormal(Set<Collision> collisions) {
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

    private static Pair<Set<Collision>, Double> findEarliestCollisions(Set<Collision> collisions) {
        // First find the minimum time among all collisions.
        double minTimeToCollision = Double.MAX_VALUE;

        for (Collision collision : collisions) {
            double timeToCollision = collision.getTimeToCollision();
            if (timeToCollision <= minTimeToCollision) {
                minTimeToCollision = timeToCollision;
            }
        }

        // Next find the earliest ones among all collision.
        // Note that there may be two collisions that will happen at the same time
        // that is why we have a set here.
        Set<Collision> earliestCollisions = new HashSet<>();

        // Find the earliest collisions here.
        for (Collision collision : collisions) {
            if (collision.getTimeToCollision() <= minTimeToCollision) {
                earliestCollisions.add(collision);
            }
        }

        return new Pair<>(earliestCollisions, minTimeToCollision);
    }
}
