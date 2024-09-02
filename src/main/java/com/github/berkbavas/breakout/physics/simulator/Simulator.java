package com.github.berkbavas.breakout.physics.simulator;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import com.github.berkbavas.breakout.physics.simulator.processor.TickProcessor;
import lombok.Getter;

import java.util.Set;

@Getter
public class Simulator {
    private final TickProcessor processor;
    private final GravityEngine gravityEngine;
    private final boolean isDebugMode;
    private double simulationTime = 0.0;

    public Simulator(World world, Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        this.processor = new TickProcessor(world, colliders, ball, isDebugMode);
        this.gravityEngine = new GravityEngine(colliders, ball);
        this.isDebugMode = isDebugMode;
    }

    public Tick<? extends Collision> update(double deltaTime) {
        var tick = processor.update(deltaTime);
        if (isDebugMode) {
            gravityEngine.update(tick);
        }
        simulationTime += tick.getTimeSpent();
        return tick;
    }

}
