package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.StationaryTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.Set;

public class PresentCollisionResolver extends CollisionResolver<PresentCollision> {

    public PresentCollisionResolver(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        super(colliders, ball, isDebugMode);
    }

    @Override
    public boolean isApplicable() {
        return !presents.isEmpty() || ball.isStationary();
    }

    @Override
    public Tick<? extends Collision> resolve(double deltaTime) {
        if (ball.isStationary()) {
            netForceCalculator.process(ball, deltaTime);
            return new StationaryTick<>(presents, deltaTime);
        }

        var target = presents.get(0);
        Vector2D normal = target.getNormal();

        if (isDebugMode) {
            ball.collide(normal, Constants.Ball.RESTITUTION_FACTOR, target.getCollider().getFrictionCoefficient());
        } else {
            ball.collide(normal);
        }

        return new CrashTick<>(presents, normal, 0);
    }
}
