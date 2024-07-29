package com.github.berkbavas.breakout.shapes.base;

import lombok.Getter;

public class Rectangle {
    protected Point2D topLeft;
    @Getter
    protected float width;
    @Getter
    protected float height;

    public Rectangle(float x, float y, float width, float height) {
        this.topLeft = new Point2D(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("Rectangle{x = %.2f, y = %.2f, width = %.2f, height = %.2f}",
                topLeft.getX(), topLeft.getY(), width, height);
    }

    public float getX() {
        return topLeft.getX();
    }

    public float getY() {
        return topLeft.getY();
    }

    public boolean contains(Point2D point) {
        return contains(point.getX(), point.getY());
    }

    public boolean contains(float x, float y) {
        float xMin = topLeft.getX();
        float xMax = topLeft.getX() + width;
        float yMin = topLeft.getY();
        float yMax = topLeft.getY() + height;

        boolean xBound = xMin <= x && x <= xMax;
        boolean yBound = yMin <= y && y <= yMax;

        return xBound && yBound;
    }
}
