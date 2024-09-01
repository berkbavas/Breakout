package com.github.berkbavas.breakout.physics.simulator.helper;

import com.github.berkbavas.breakout.math.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CuttingCriticalPointPair extends CriticalPointPair {
    private final List<Point2D> points;


    @Override
    public Point2D getPointOnCircle() {
        return points.get(0);
    }

    @Override
    public Point2D getPointOnEdge() {
        return points.get(0);
    }
}
