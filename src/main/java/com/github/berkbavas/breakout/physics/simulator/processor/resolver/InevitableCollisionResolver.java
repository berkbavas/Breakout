package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;

import java.util.stream.Collectors;

public class InevitableCollisionResolver extends CollisionResolver<InevitableCollision> {
    public InevitableCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }

    @Override
    public boolean isApplicable() {
        return !inevitables.isEmpty();
    }

    @Override
    public CrashTick<InevitableCollision> resolve(double deltaTime) {
        ProspectiveCollision earliest = CollisionEngine.findEarliestCollision(inevitables);
        Collider collider = earliest.getCollider();
        var filtered = inevitables.stream().filter(collision -> collision.getCollider() == collider).collect(Collectors.toList());

        double ttc = earliest.getTimeToCollision();
        Vector2D velocity = ball.getVelocity();
        Vector2D normal = CollisionEngine.calculateCollectiveCollisionNormal(filtered, velocity);

        if (normal.l2norm() == 0) {
            // TODO: Think about a smart solution for this case.
            ball.move(ttc);
        } else {
            ball.move(ttc);

            if (isDebugMode) {
                ball.collide(normal, collider.getRestitutionFactor());
            } else {
                ball.collide(normal);
            }
        }

        return new CrashTick<>(filtered, normal, ttc);
    }

}
