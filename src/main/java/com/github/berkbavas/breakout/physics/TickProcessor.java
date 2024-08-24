package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Collider;
import com.github.berkbavas.breakout.physics.node.World;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TickProcessor {
    private final GameObjects objects;

    public TickProcessor(GameObjects objects) {
        this.objects = objects;
    }

    // Clamps ball inside the world.
    // Make sure that collision impact does not lead the ball getting out of the world.
    public void preTick() {
        Ball ball = objects.getBall();
        World world = objects.getWorld();

        Point2D center = ball.getCenter();
        double radius = ball.getRadius();
        double padding = radius + 2 * Util.EPSILON;
        double x = Util.clamp(world.getLeftTop().getX() + padding, center.getX(), world.getRightTop().getX() - padding);
        double y = Util.clamp(world.getLeftTop().getY() + padding, center.getY(), world.getLeftBottom().getY() - padding);

        Point2D clampedCenter = new Point2D(x, y);

        if (!center.equals(clampedCenter)) {
            ball.setCenter(clampedCenter);
        }
    }

    public TickResult nextTick(double deltaTime) {
        final Ball ball = objects.getBall();
        final Set<Collision> allCollisions = findAllCollisions();
        final double minimumTimeToCollision = findMinimumTimeToCollision(allCollisions);

        final Set<Collision> earliestCollisions = allCollisions.stream()
                .filter(collision -> collision.getTimeToCollision() <= minimumTimeToCollision)
                .collect(Collectors.toSet());

        // Will a collision occur in the given delta time?
        final boolean collides = minimumTimeToCollision <= deltaTime;

        if (!collides) {
            // We are good to go. No collision in delta time.
            ball.move(deltaTime);
            return new TickResult(false, deltaTime, earliestCollisions);
        }

        // If we are here a collision will happen in the given delta time.

        // Make sure that timeToCollision is not zero.
        // If it is zero, then it means that we are already colliding.
        // Just move the ball a little bit in order to get rid of the -sticky- collision.
        final double clampedTimeToCollision = Util.clamp(Util.EPSILON, minimumTimeToCollision, deltaTime);
        TickResult result = new TickResult(true, clampedTimeToCollision, earliestCollisions);

        // Who is the collider?
        Collider collider = result.getCollider().orElse(null);
        assert collider != null;

        // Calculate the collision normal
        Vector2D normal = calculateCollisionNormal(earliestCollisions, ball.getVelocity());

        // Process the ball
        ball.move(clampedTimeToCollision);
        ball.collide(normal);
        ball.translate(normal, collider.getCollisionImpactFactor());

        return result;
    }

    public Set<Collision> findEarliestCollisions() {
        final Set<Collision> allCollisions = findAllCollisions();
        final double minimumTimeToCollision = findMinimumTimeToCollision(allCollisions);

        return allCollisions.stream()
                .filter(collision -> collision.getTimeToCollision() <= minimumTimeToCollision)
                .collect(Collectors.toSet());
    }

    public Set<Collision> findAllCollisions() {
        Ball ball = objects.getBall();
        Vector2D velocity = ball.getVelocity();
        Set<Collider> colliders = objects.getColliders();

        Set<Collision> collisions = new HashSet<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            collisions.addAll(collider.findCollisions(ball, velocity));
        }

        return collisions;
    }

    private static Vector2D calculateCollisionNormal(Set<Collision> collisions, Vector2D velocity) {
        Vector2D collisionNormal = new Vector2D(0, 0);

        for (Collision collision : collisions) {
            Collider node = collision.getCollider();
            LineSegment2D edge = collision.getEdge();
            Vector2D normal = node.getNormalOf(edge);

            double dot = normal.dot(velocity);
            // We only consider normals whose dot product with the velocity vector is negative.
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

    private static double findMinimumTimeToCollision(Set<Collision> collisions) {
        double minTimeToCollision = Double.MAX_VALUE;

        for (Collision collision : collisions) {
            final double timeToCollision = collision.getTimeToCollision();
            if (timeToCollision <= minTimeToCollision) {
                minTimeToCollision = timeToCollision;
            }
        }

        return minTimeToCollision;
    }
}
