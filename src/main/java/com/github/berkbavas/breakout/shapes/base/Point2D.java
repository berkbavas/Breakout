package com.github.berkbavas.breakout.shapes.base;

public class Point2D {
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Point2D{x = %.2f, y = %.2f}", x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
