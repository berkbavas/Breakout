package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.CollisionEngine;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class InevitableCollisionResolver extends CollisionResolver<InevitableCollision> {

    public InevitableCollisionResolver(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        super(colliders, ball, isDebugMode);
    }

    @Override
    public boolean isApplicable() {
        return !inevitables.isEmpty();
    }

    @Override
    public CrashTick<InevitableCollision> resolve(double deltaTime) {
        assert !inevitables.isEmpty();

        var sorted = inevitables.stream()
                .sorted(Comparator.comparingDouble(InevitableCollision::getTimeToCollision))
                .collect(Collectors.toCollection(ArrayList::new));

        InevitableCollision earliest = sorted.get(0);
        Collider collider = earliest.getCollider();

        var filtered = inevitables.stream()
                .filter(collision -> collision.getCollider() == collider)
                .collect(Collectors.toCollection(ArrayList::new));

        double ttc = earliest.getTimeToCollision();
        Vector2D velocity = ball.getVelocity();
        Vector2D normal = CollisionEngine.calculateCollectiveCollisionNormal(filtered, velocity);


        if (isDebugMode) {
            var result = netForceCalculator.process(ball, ttc);

            if (normal.l2norm() != 0) {
                ball.collide(normal, Constants.Ball.RESTITUTION_FACTOR[0], collider.getFrictionCoefficient());
            }

        } else {
            ball.move(deltaTime);
            ball.collide(normal);
        }

        return new CrashTick<>(filtered, normal, ttc);
    }

}
