package com.github.berkbavas.breakout.shapes.base;

import lombok.Getter;

@Getter
public class Point2D {
    protected float x;
    protected float y;

    public Point2D() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2D add(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D subtract(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }

    @Override
    public String toString() {
        return String.format("Point2D{x = %.3f, y = %.3f}", x, y);
    }

}
