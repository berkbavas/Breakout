package com.github.berkbavas.breakout.physics.simulator;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import com.github.berkbavas.breakout.physics.simulator.processor.TickProcessor;
import lombok.Getter;

import java.util.Set;

@Getter
public class Simulator {
    private final TickProcessor processor;
    private final boolean isDebugMode;
    private double simulationTime = 0.0;

    public Simulator(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        this.processor = new TickProcessor(colliders, ball, isDebugMode);
        this.isDebugMode = isDebugMode;
    }

    public Tick<? extends Collision> process(double deltaTime) {
        var result = processor.process(deltaTime);
        simulationTime += result.getTimeSpent();
        result.setSimulationTime(simulationTime);
        return result;
    }

}
