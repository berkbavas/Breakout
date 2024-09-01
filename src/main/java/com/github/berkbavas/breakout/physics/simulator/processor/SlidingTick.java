package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SlidingTick extends Tick<PresentCollision> {

    public SlidingTick(List<PresentCollision> collisions, double timeSpent) {
        super(collisions, timeSpent);
    }
}
