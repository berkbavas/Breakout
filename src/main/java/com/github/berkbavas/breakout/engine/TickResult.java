package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.ColliderNode;
import lombok.Getter;

import java.util.Set;

@Getter
public class TickResult {
    private final boolean isCollided;
    private final double timeConsumed;
    private final Set<Collision> collisions;
    private final ColliderNode collider;

    public TickResult(boolean isCollided, double timeConsumed, Set<Collision> collisions) {
        this.isCollided = isCollided;
        this.timeConsumed = timeConsumed;
        this.collisions = collisions;
        this.collider = findCollider(collisions);
    }

    public static ColliderNode findCollider(Set<Collision> collisions) {
        for (Collision collision : collisions) {
            return collision.getCollider();
        }
        return null;
    }
}

