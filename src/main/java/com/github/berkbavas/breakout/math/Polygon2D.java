package com.github.berkbavas.breakout.math;

import java.util.List;

public class Polygon2D extends AbstractPolygon2D<LineSegment2D> {

    public Polygon2D(List<Point2D> vertices, List<String> identifiers) {
        super(vertices, identifiers);
    }

    public Polygon2D(List<Point2D> vertices) {
        super(vertices);
    }

    @Override
    protected LineSegment2D createEdge(Point2D P, Point2D Q, String identifier) {
        return new LineSegment2D(P, Q, identifier);
    }
}
