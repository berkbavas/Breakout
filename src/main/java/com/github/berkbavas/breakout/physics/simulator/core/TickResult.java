package com.github.berkbavas.breakout.physics.simulator.core;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import lombok.Getter;

import java.util.Set;

@Getter
public class TickResult {

    public enum Status {
        MOVING,
        COLLIDED,
        STEADY
    }

    private final Status status;
    private final double consumedTime;
    private final Set<Collision> collisions;
    private final Vector2D collisionNormal;

    public TickResult(Status status, double consumedTime, Set<Collision> collisions, Vector2D collisionNormal) {
        this.status = status;
        this.consumedTime = consumedTime;
        this.collisions = collisions;
        this.collisionNormal = collisionNormal;
    }

    public Collider getCollider() {
        for (Collision collision : collisions) {
            return collision.getCollider();
        }
        return null;
    }

    public double getMinimumTimeToCollision() {
        return CollisionEngine.findMinimumTimeToCollision(collisions);
    }

    public boolean isAlreadyColliding() {
        for (Collision collision : collisions) {
            if (collision.isAlreadyColliding()) {
                return true;
            }
        }

        return false;
    }
}

