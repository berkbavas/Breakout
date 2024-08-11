package com.github.berkbavas.breakout.math;

import lombok.Getter;

@Getter
public class Point2D {
    protected double x;
    protected double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D add(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D subtract(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }

    public double distanceTo(Point2D other) {
        return distanceBetween(this, other);
    }

    public static double distanceBetween(Point2D a, Point2D b) {
        final double dx = (a.x - b.x);
        final double dy = (a.y - b.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2D toVector2D() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point2D{x = %.3f, y = %.3f}", x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Point2D) {
            Point2D other = (Point2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        } else {
            return false;
        }
    }
}
