package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.AbstractRectangle2D;
import com.github.berkbavas.breakout.math.Point2D;

public abstract class RectangularNode extends AbstractRectangle2D<ColliderEdge> implements ColliderNode {

    public RectangularNode(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    protected ColliderEdge createEdge(Point2D P, Point2D Q, String identifier) {
        return new ColliderEdge(P, Q, identifier);
    }
}
