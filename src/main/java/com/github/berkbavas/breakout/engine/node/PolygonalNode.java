package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.AbstractPolygon2D;
import com.github.berkbavas.breakout.math.Point2D;

import java.util.List;

public abstract class PolygonalNode extends AbstractPolygon2D<ColliderEdge> implements ColliderNode {

    public PolygonalNode(List<Point2D> vertices, List<String> identifiers) {
        super(vertices, identifiers);
    }

    public PolygonalNode(List<Point2D> vertices) {
        super(vertices);
    }

    @Override
    protected ColliderEdge createEdge(Point2D P, Point2D Q, String identifier) {
        return new ColliderEdge(P, Q, identifier);
    }
}
