package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.StaticNode;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;

import java.util.Set;

@Getter
public class TickResult {
    private final boolean isCollided;
    private final double timeConsumed;
    private final Set<Collision> collisions;
    private final StaticNode collider;

    public TickResult(boolean isCollided, double timeConsumed, Set<Collision> collisions) {
        this.isCollided = isCollided;
        this.timeConsumed = timeConsumed;
        this.collisions = collisions;
        this.collider = findCollider(collisions);
    }

    public static StaticNode findCollider(Set<Collision> collisions) {
        for (Collision collision : collisions) {
            return collision.getCollider();
        }
        return null;
    }
}

