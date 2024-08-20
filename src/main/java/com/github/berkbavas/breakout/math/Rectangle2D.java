package com.github.berkbavas.breakout.math;

public class Rectangle2D extends AbstractRectangle2D<LineSegment2D> {

    public Rectangle2D(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    protected LineSegment2D createEdge(Point2D P, Point2D Q, String identifier) {
        return new LineSegment2D(P, Q, identifier);
    }
}
