package com.github.berkbavas.breakout.physics.simulator.helper;

import com.github.berkbavas.breakout.math.Point2D;

public abstract class CriticalPointPair {

    public abstract Point2D getPointOnCircle();

    public abstract Point2D getPointOnEdge();

    public double getDistance() {
        return 0;
    }
}
