package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.Optional;

@Getter
public class LineSegment2D {
    private final Point2D P;
    private final Point2D Q;

    public LineSegment2D(Point2D P, Point2D Q) {
        this.P = P;
        this.Q = Q;
    }

    public boolean isPointOnLineSegment(Point2D point) {
        double length = length();
        double distance = P.distanceTo(point) + Q.distanceTo(point);

        if (Util.fuzzyCompare(length, distance)) {
            Vector2D expectedDirection = new Vector2D(point.x - P.x, point.y - P.y);
            Vector2D direction = direction();
            return direction.isCollinear(expectedDirection);
        } else {
            return false;
        }
    }

    public double length() {
        return P.distanceTo(Q);
    }

    public Vector2D direction() {
        return new Vector2D(Q.x - P.x, Q.y - P.y);
    }

    public Optional<Point2D> findIntersection(Ray2D ray) {
        return ray.findIntersection(this);
    }

    @Override
    public String toString() {
        return String.format("LineSegment2D{P = %s, Q = %s}", P, Q);
    }
}
