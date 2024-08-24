package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.physics.node.Collider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Getter
public class TickResult {
    private final boolean collided;
    private final double timeConsumed;
    private final Set<Collision> collisions;

    public Optional<Collider> getCollider() {
        for (Collision collision : collisions) {
            return Optional.of(collision.getCollider());
        }
        return Optional.empty();
    }
}

