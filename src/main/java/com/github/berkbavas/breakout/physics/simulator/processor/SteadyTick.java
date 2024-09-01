package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;

import java.util.List;

public class SteadyTick extends Tick<PresentCollision> {

    public SteadyTick(List<PresentCollision> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }
}
