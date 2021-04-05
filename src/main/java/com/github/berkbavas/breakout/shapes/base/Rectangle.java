package com.github.berkbavas.breakout.shapes.base;

public class Rectangle {
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("Rectangle{x = %.2f, y = %.2f, width = %.2f, height = %.2f}", x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean contains(Point2D point) {
        return x <= point.getX() && point.getX() <= x + width && y <= point.getY() && point.getY() <= y + height;
    }

    public boolean contains(double x, double y) {
        return this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + height;
    }

}
