package com.github.berkbavas.breakout.physics.simulator;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.core.Collision;
import com.github.berkbavas.breakout.physics.simulator.core.TickProcessor;
import com.github.berkbavas.breakout.physics.simulator.core.TickResult;
import lombok.Getter;

import java.util.Set;

@Getter
public class Simulator {
    private final TickProcessor processor;
    private double simulationTime = 0.0;

    public Simulator(World world, Set<Collider> colliders, Ball ball) {
        this.processor = new TickProcessor(world, colliders, ball);
    }

    public TickResult update(double deltaTime) {
        TickResult result = processor.update(deltaTime);
        simulationTime += result.getConsumedTime();
        return result;
    }

    public Set<Collision> findEarliestCollisions(double deltaTime) {
        return processor.findEarliestCollisions(deltaTime);
    }
}
