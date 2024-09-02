package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.SteadyTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

public class PresentCollisionResolver extends CollisionResolver<PresentCollision> {

    public PresentCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }

    @Override
    public boolean isApplicable() {
        return !presents.isEmpty();
    }

    @Override
    public Tick<? extends Collision> resolve(double deltaTime) {
        if (ball.isSteady()) {
            return new SteadyTick<>(presents, deltaTime);
        }

        Vector2D velocity = ball.getVelocity();

        if (velocity.l2norm() == 0) {
            // Ball is still.
            // Call move() in order to update the velocity since the acceleration is not zero.
            ball.move(deltaTime);
            return new SteadyTick<>(presents, deltaTime);
        }

        assert !presents.isEmpty();

        // Sort most effective to the least effective
        presents.sort((c0, c1) -> {
            double cor0 = c0.getCollider().getRestitutionFactor();
            double cor1 = c1.getCollider().getRestitutionFactor();
            return Double.compare(cor0, cor1);
        });

        PresentCollision target = CollisionEngine.findMostEffectiveCollision(presents);

        Vector2D normal = target.getNormal();
        double restitutionFactor = target.getCollider().getRestitutionFactor();

        if (isDebugMode) {
            ball.collide(normal, restitutionFactor);
        } else {
            ball.collide(normal);
        }

        return new CrashTick<>(presents, normal, 0);
    }
}
