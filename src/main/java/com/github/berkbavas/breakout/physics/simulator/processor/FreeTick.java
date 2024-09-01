package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;

import java.util.List;

public class FreeTick extends Tick<PotentialCollision> {

    public FreeTick(List<PotentialCollision> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }
}
