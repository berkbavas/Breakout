package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.processor.resolver.InevitableCollisionResolver;
import com.github.berkbavas.breakout.physics.simulator.processor.resolver.PotentialCollisionResolver;
import com.github.berkbavas.breakout.physics.simulator.processor.resolver.PresentCollisionResolver;

import java.util.List;
import java.util.Set;

public class TickProcessor {
    private final CollisionEngine collisionEngine;

    private final InevitableCollisionResolver inevitableCollisionResolver;
    private final PresentCollisionResolver presentCollisionResolver;
    private final PotentialCollisionResolver potentialCollisionResolver;

    public TickProcessor(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        collisionEngine = new CollisionEngine(colliders, ball);
        inevitableCollisionResolver = new InevitableCollisionResolver(colliders, ball, isDebugMode);
        presentCollisionResolver = new PresentCollisionResolver(colliders, ball, isDebugMode);
        potentialCollisionResolver = new PotentialCollisionResolver(colliders, ball, isDebugMode);
    }

    public Tick<? extends Collision> process(double deltaTime) {
        List<Collision> collisions = collisionEngine.findCollisions(deltaTime);

        presentCollisionResolver.load(collisions);
        inevitableCollisionResolver.load(collisions);
        potentialCollisionResolver.load(collisions);

        if (presentCollisionResolver.isApplicable()) {
            return presentCollisionResolver.resolve(deltaTime);
        }

        if (inevitableCollisionResolver.isApplicable()) {
            return inevitableCollisionResolver.resolve(deltaTime);
        }

        return potentialCollisionResolver.resolve(deltaTime);
    }
}
