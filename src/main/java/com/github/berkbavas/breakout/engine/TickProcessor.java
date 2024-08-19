package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.StaticNode;
import com.github.berkbavas.breakout.engine.node.World;
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

    // This method clamps the center of the ball inside the world.
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

    public void updatePaddle(Point2D newPreferredTopLeft) {
        Ball ball = gameObjects.getBall();
        Paddle paddle = gameObjects.getPaddle();
        Paddle newPaddle = paddle.getNewPaddleByTakingCareOfCollision(newPreferredTopLeft, ball);
        gameObjects.setPaddle(newPaddle);
    }

    public TickResult nextTick(double deltaTime) {
        final Set<Collision> potentialCollisions = CollisionDetector.findPotentialCollisions(gameObjects);

        final Pair<Set<Collision>, Double> pairs = findEarliestCollisions(potentialCollisions);
        final Set<Collision> earliestCollisions = pairs.getKey();
        final double timeToCollision = pairs.getValue();

        if (earliestCollisions.isEmpty()) {
            // We are good, there is no collision.
            moveBall(deltaTime);

            // Return an empty set as there is no collision.
            return new TickResult(false, deltaTime, Set.of());
        }

        final boolean collides = timeToCollision <= deltaTime;

        if (!collides) {
            // If we are here a collision won't happen in the given delta time.
            moveBall(deltaTime);

            return new TickResult(false, deltaTime, earliestCollisions);
        }

        // If we are here a collision will happen in the given delta time.

        // Make sure that timeToCollision is not zero.
        // If it is zero, then it means that we are already colliding.
        // Just move the ball a little bit in order to get rid of the -sticky- collision.
        final double clampedTimeToCollision = Util.clamp( Util.EPSILON, timeToCollision, deltaTime);

        // Calculate the collision normal
        Vector2D collisionNormal = calculateCollisionNormal(earliestCollisions, gameObjects.getBall());

        // Who is the collider?
        StaticNode collider = TickResult.findCollider(earliestCollisions);
        assert collider != null;

        // Process the ball
        collideBall(collider, collisionNormal, clampedTimeToCollision);

        return new TickResult(true, clampedTimeToCollision, earliestCollisions);
    }

    public static Vector2D calculateCollisionNormal(Set<Collision> collisions, Ball ball) {
        Vector2D collisionNormal = new Vector2D(0, 0);
        Vector2D velocity = ball.getVelocity();

        for (Collision collision : collisions) {
            StaticNode node = collision.getCollider();
            LineSegment2D edge = collision.getEdge();
            Vector2D normal = node.getNormalFor(edge);

            double dot = normal.dot(velocity);
            // We only consider normals whose dot product with the velocity is negative.
            // Otherwise, reflection of the velocity vector w.r.t. collision normal does not make any sense.
            if (dot < 0.0) {
                collisionNormal = collisionNormal.add(normal);
            } else {
                // TODO: Remove this after tests
                System.out.println(collision);
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

    private void moveBall(double deltaTime) {
        Ball ball = gameObjects.getBall();
        Ball newBall = ball.move(deltaTime);
        gameObjects.setBall(newBall);
    }

    private void collideBall(StaticNode collider, Vector2D collisionNormal, double timeToCollision) {
        Ball ball = gameObjects.getBall();
        Ball newBall = ball.collide(collisionNormal, timeToCollision, collider.getCollisionImpactFactor());
        gameObjects.setBall(newBall);
    }

}
