package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.FreeTick;

import java.util.Set;

public class PotentialCollisionResolver extends CollisionResolver<PotentialCollision> {

    public PotentialCollisionResolver(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        super(colliders, ball, isDebugMode);
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

        if (isDebugMode) {
            var result = netForceCalculator.process(ball, deltaTime);

            if (result.getType() == NetForceCalculator.ResultType.NET_ZERO) {
                ball.move(deltaTime);
            }
        } else {
            ball.move(deltaTime);
        }

        return new FreeTick<>(potentials, deltaTime);
    }
}
