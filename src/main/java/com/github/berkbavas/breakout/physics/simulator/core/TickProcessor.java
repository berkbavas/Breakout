package com.github.berkbavas.breakout.physics.simulator.core;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class TickProcessor {
    @Getter
    private final CollisionEngine collisionEngine;

    @Getter
    private final GravityEngine gravityEngine;

    private final World world;
    private final Ball ball;


    public TickProcessor(World world, Set<Collider> colliders, Ball ball) {
        this.collisionEngine = new CollisionEngine(colliders, ball);
        this.gravityEngine = new GravityEngine(colliders, ball);
        this.world = world;
        this.ball = ball;
    }

    public TickResult update(double deltaTime) {
        preTick();
        TickResult result = nextTick(deltaTime);
        gravityEngine.update(result, deltaTime);

        return result;
    }

    // Clamps ball inside the world.
    // Make sure that collision impact does not lead the ball getting out of the world.
    private void preTick() {
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

    private TickResult nextTick(double deltaTime) {
        if (ball.isSteady()) {
            return new TickResult(TickResult.Status.STEADY, deltaTime, Set.of(), null);
        }

        final Set<Collision> allCollisions = collisionEngine.findCollisions(deltaTime);
        final double minimumTimeToCollision = CollisionEngine.findMinimumTimeToCollision(allCollisions);

        final Set<Collision> earliestCollisions = allCollisions.stream()
                .filter(collision -> collision.getTimeToCollision() <= minimumTimeToCollision)
                .collect(Collectors.toSet());

        // TODO: Check that ball is already colliding.
        // Note that an exception occurs when the velocity is zero.
        // Because ColliderEdge.findClosestPairsAlongGivenDirection() assumes that direction is not zero!

        // Will a collision occur in the given delta time?
        final boolean collides = minimumTimeToCollision <= deltaTime;

        if (!collides) {
            // We are good to go. No collision in delta time.
            ball.move(deltaTime);
            return new TickResult(TickResult.Status.MOVING, deltaTime, earliestCollisions, null);
        }

        // If we are here a collision will happen in the given delta time.

        // Make sure that timeToCollision is not zero.
        // If it is zero, then it means that we are already colliding.
        // Just move the ball a little bit in order to get rid of the -sticky- collision.
        final double timeToCollision = Util.clamp(0, minimumTimeToCollision, deltaTime);

        final Vector2D velocity = ball.getNextVelocity(timeToCollision);

        // Calculate the collision normal
        final Vector2D normal = CollisionEngine.calculateCollectiveCollisionNormal(earliestCollisions, velocity);
        assert 0 < normal.l2norm();

        TickResult result = new TickResult(TickResult.Status.COLLIDED, timeToCollision, earliestCollisions, normal);

        // Process the ball
        ball.move(timeToCollision); // Move until ball touches the collider.

        // Collision!
        // This is where the velocity is reflected.
        // Ball loses some speed according to the normal of collision and damping factor of the collider.
        Collider collider = result.getCollider();
        assert collider != null;

        ball.collide(normal, collider.getRestitutionFactor());

        return result;
    }

    public Set<Collision> findEarliestCollisions(double deltaTime) {
        return collisionEngine.filterEarliestCollisions(deltaTime);
    }

}
