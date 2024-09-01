package com.github.berkbavas.breakout.physics.simulator.helper;

import com.github.berkbavas.breakout.math.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeparateCriticalPointPair extends CriticalPointPair {
    private final Point2D pointOnCircle;
    private final Point2D pointOnEdge;

    @Override
    public double getDistance() {
        return Point2D.distanceBetween(pointOnEdge, pointOnCircle);
    }
}