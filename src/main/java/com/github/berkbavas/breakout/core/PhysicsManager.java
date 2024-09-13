package com.github.berkbavas.breakout.core;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.Simulator;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.PausedTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhysicsManager extends Manager {
    private final Simulator simulator;
    private final AtomicBoolean next = new AtomicBoolean(false);
    private Tick<? extends Collision> result = new PausedTick();

    public PhysicsManager(GameObjects objects, boolean isDebugMode) {
        final Set<Collider> colliders = objects.getColliders();
        final Ball ball = objects.getBall();
        this.simulator = new Simulator(colliders, ball, isDebugMode);
    }

    public Tick<? extends Collision> update() {
        if (next.get()) {
            next.set(false);
            return updatePrivate();
        }
        if (isPaused()) {
            return result;
        }

        return updatePrivate();
    }

    private Tick<? extends Collision> updatePrivate() {
        final double deltaTime = Constants.Physics.SIMULATION_RATIO[0];
        result = simulator.process(deltaTime);

        // Check if a brick is hit in this particular tick.
        updateBricks(result);

        return result;
    }

    private void updateBricks(Tick<? extends Collision> result) {
        if (result instanceof CrashTick) {
            var collisions = result.getCollisions();
            for (Collision collision : collisions) {
                Collider collider = collision.getCollider();

                if (collider instanceof Brick) {
                    Brick brick = (Brick) collider;
                    brick.setHit(true);
                }
            }
        }
    }

    public void next() {
        next.set(true);
    }
}
