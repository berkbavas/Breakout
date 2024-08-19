package com.github.berkbavas.breakout.math;

public class Vector2D extends Point2D {

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D reversed() {
        return this.multiply(-1.0);
    }

    public Vector2D normalized() {
        final double l2norm = l2norm();

        double x = this.x;
        double y = this.y;

        if (!Util.fuzzyCompare(l2norm, 1.0) && !Util.isFuzzyZero(l2norm)) {
            double norm = Math.sqrt(l2norm);
            x = x / norm;
            y = y / norm;
        }

        return new Vector2D(x, y);
    }

    public Vector2D reflect(Vector2D normal) {
        // Reflected vector is given by the following equation.
        // r = v - 2 * (v ∙ n) * n
        normal = normal.normalized();
        final double dot = dot(this, normal);
        return this.subtract(normal.multiply(2.0 * dot));
    }

    public double dot(Vector2D other) {
        return dot(this, other);
    }

    public static double dot(Vector2D a, Vector2D b) {
        return a.x * b.x + a.y * b.y;
    }

    public boolean isCollinear(Vector2D other) {
        Vector2D normal = normal();
        double dot = normal.dot(other);
        return Util.isFuzzyZero(dot);
    }

    public Vector2D normal() {
        return new Vector2D(-y, x);
    }

    public double length() {
        return Math.pow(Math.pow(x, 2.0) + Math.pow(y, 2.0), 0.5);
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public double l2norm() {
        return x * x + y * y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2D) {
            Vector2D other = (Vector2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        }
        if (object instanceof Point2D) {
            Point2D other = (Point2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        } else {
            return false;
        }
    }

    public Vector2D projectOnto(Vector2D other) {
        double dot = dot(other);
        double l2 = other.l2norm();
        return other.multiply(dot / l2);
    }

    @Override
    public String toString() {
        return String.format("Vector2D{x = %.2f, y = %.2f}", x, y);
    }
}
