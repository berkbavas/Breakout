package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
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
    private final World world;
    private final Set<Collider> colliders;
    private final Ball ball;

    private final InevitableCollisionResolver inevitableCollisionResolver;
    private final PresentCollisionResolver presentCollisionResolver;
    private final PotentialCollisionResolver potentialCollisionResolver;

    public TickProcessor(World world, Set<Collider> colliders, Ball ball) {
        this.world = world;
        this.colliders = colliders;
        this.ball = ball;

        collisionEngine = new CollisionEngine(colliders, ball);
        inevitableCollisionResolver = new InevitableCollisionResolver(ball);
        presentCollisionResolver = new PresentCollisionResolver(ball);
        potentialCollisionResolver = new PotentialCollisionResolver(ball);
    }

    public Tick<? extends Collision> update(double deltaTime) {
        List<Collision> collisions = collisionEngine.findCollisions(deltaTime);

        inevitableCollisionResolver.load(collisions);
        presentCollisionResolver.load(collisions);
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