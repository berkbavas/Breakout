package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;

import java.util.ArrayList;

public class PausedTick extends Tick<Collision> {

    public PausedTick() {
        super(new ArrayList<>(), 0.0);
    }

    @Override
    protected String getChildName() {
        return "Paused Tick";
    }
}
