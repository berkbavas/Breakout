package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;

import java.util.ArrayList;

public class FreeTick<T extends Collision> extends Tick<T> {

    public FreeTick(ArrayList<T> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }

    @Override
    protected String getChildName() {
        return "Free Tick";
    }
}
