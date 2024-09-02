package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.FreeTick;

public class PotentialCollisionResolver extends CollisionResolver<PotentialCollision> {

    public PotentialCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }


    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public FreeTick<PotentialCollision> resolve(double deltaTime) {
        // Ball is free, there is no interactions with the colliders, i.e.,
        // no present collisions in this tick and no inevitable collisions until deltaTime.

        // There is nothing to resolve.
        // Just move the ball.
        ball.move(deltaTime);

        return new FreeTick<>(potentials, deltaTime);
    }
}
