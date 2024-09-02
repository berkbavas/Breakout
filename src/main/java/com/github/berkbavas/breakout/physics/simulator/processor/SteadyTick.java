package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;

import java.util.List;

public class SteadyTick<T extends Collision> extends Tick<T> {

    public SteadyTick(List<T> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }
}
