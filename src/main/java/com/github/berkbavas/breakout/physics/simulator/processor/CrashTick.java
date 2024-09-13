package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class CrashTick<T extends Collision> extends Tick<T> {
    private final Vector2D normal;

    public CrashTick(ArrayList<T> collisions, Vector2D normal, double timeSpent) {
        super(collisions, timeSpent);
        this.normal = normal;
    }

    @Override
    protected String getChildName() {
        return "Crash Tick";
    }
}
