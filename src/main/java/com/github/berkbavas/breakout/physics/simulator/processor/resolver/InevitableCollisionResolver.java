package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;

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
        double timeToCollision = earliest.getTimeToCollision();
        Vector2D velocity = ball.getVelocity();
        Vector2D normal = CollisionEngine.calculateCollectiveCollisionNormal(inevitables, velocity);

        if (normal.l2norm() == 0) {
            // TODO: Think about a smart solution for this case.
            ball.move(timeToCollision);
        } else {
            ball.move(timeToCollision);

            if (isDebugMode) {
                ball.collide(normal, collider.getRestitutionFactor());
            } else {
                ball.collide(normal);
            }
        }

        return new CrashTick<>(inevitables, normal, timeToCollision);
    }

}
