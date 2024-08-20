package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.List;

public class Obstacle extends PolygonalNode {

    public Obstacle(List<Point2D> vertices, List<String> identifiers) {
        super(vertices, identifiers);
    }

    public Obstacle(List<Point2D> vertices) {
        super(vertices);
    }

    @Override
    public double getCollisionImpactFactor() {
        return 0;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return null;
    }
}
