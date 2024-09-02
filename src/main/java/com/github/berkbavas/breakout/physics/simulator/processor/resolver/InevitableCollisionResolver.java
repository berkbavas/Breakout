package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;

import java.util.List;

public class InevitableCollisionResolver extends CollisionResolver<InevitableCollision> {
    public InevitableCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }

    @Override
    public void load(List<Collision> collisions) {
        targets.clear();

        for (Collision collision : collisions) {
            if (collision instanceof InevitableCollision) {
                InevitableCollision target = (InevitableCollision) collision;
                targets.add(target);
            }
        }
    }

    @Override
    public boolean isApplicable() {
        return !targets.isEmpty();
    }

    @Override
    public CrashTick<InevitableCollision> resolve(double deltaTime) {
        ProspectiveCollision earliest = findEarliestCollision(targets);
        Collider collider = earliest.getCollider();
        double timeToCollision = earliest.getTimeToCollision();
        Vector2D velocity = ball.getVelocity();
        Vector2D normal = CollisionResolver.calculateCollisionNormal(targets, velocity);

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

        return new CrashTick<>(targets, normal, timeToCollision);
    }

}
