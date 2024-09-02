package com.github.berkbavas.breakout.physics.simulator;

import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.Conflict;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class GravityEngine {
    private final Set<Collider> colliders;
    private final Ball ball;

    @Setter
    private Vector2D gravity = new Vector2D(0, 2000);

    public GravityEngine(Set<Collider> colliders, Ball ball) {
        this.colliders = colliders;
        this.ball = ball;
    }

    public void update(Tick<? extends Collision> result) {
        var collisions = CollisionEngine.findConflicts(colliders, ball);
        var filtered = filterPresentCollisions(collisions);

        if (filtered.isEmpty()) {
            ball.setAcceleration(gravity);
        } else if (filtered.size() == 1) {
            Vector2D normal = filtered.get(0).getNormal();
            Vector2D acceleration = gravity.rejectionOf(normal);  // We need rejection, not projection.
            ball.setAcceleration(acceleration);
        } else {
            ball.setAcceleration(new Vector2D(0, 0));
        }
    }

    private List<Conflict> filterPresentCollisions(List<Conflict> collisions) {
        List<Conflict> filtered = new ArrayList<>();

        for (var collision : collisions) {
            Vector2D normal = collision.getNormal();

            if (gravity.dot(normal) < -Util.EPSILON) {
                filtered.add(collision);
            }
        }

        return filtered;
    }
}
