package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;

import java.util.List;

public class FreeTick<T extends Collision> extends Tick<T> {

    public FreeTick(List<T> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }
}
