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
    private double simulationTime = 0.0;

    public Simulator(World world, Set<Collider> colliders, Ball ball) {
        this.processor = new TickProcessor(world, colliders, ball);
        this.gravityEngine = new GravityEngine(colliders, ball);
    }

    public Tick<? extends Collision> update(double deltaTime) {
        var tick = processor.update(deltaTime);
        gravityEngine.update(tick);
        simulationTime += tick.getTimeSpent();
        return tick;
    }

}
