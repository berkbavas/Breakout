package com.github.berkbavas.breakout.physics.simulator.helper;

import com.github.berkbavas.breakout.math.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TangentialCriticalPoint extends CriticalPointPair {
    private final Point2D point;

    @Override
    public Point2D getPointOnCircle() {
        return point;
    }

    @Override
    public Point2D getPointOnEdge() {
        return point;
    }
}