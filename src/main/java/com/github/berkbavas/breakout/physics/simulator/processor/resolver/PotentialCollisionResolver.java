package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.FreeTick;

import java.util.List;

public class PotentialCollisionResolver extends CollisionResolver<PotentialCollision> {

    public PotentialCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }

    @Override
    public void load(List<Collision> collisions) {
        targets.clear();

        for (Collision collision : collisions) {
            if (collision instanceof PotentialCollision) {
                PotentialCollision target = (PotentialCollision) collision;
                targets.add(target);
            }
        }
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public FreeTick resolve(double deltaTime) {

        // Ball is free, there is no interactions with the colliders, i.e.,
        // no present collisions in this tick and no inevitable collisions until deltaTime.

        // Just move the ball.
        // Do not touch the acceleration. It will be resolved in next tick if there is any interactions.

        ball.move(deltaTime);
        return new FreeTick(targets, deltaTime);
    }
}
