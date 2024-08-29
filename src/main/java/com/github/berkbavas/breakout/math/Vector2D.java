package com.github.berkbavas.breakout.math;

import lombok.Getter;

@Getter
public class Vector2D {
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(scalar * x, scalar * y);
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
        return Util.isFuzzyZero(normal().dot(other));
    }

    public Vector2D normal() {
        return new Vector2D(-y, x);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double norm() {
        return length();
    }

    public double l2norm() {
        return x * x + y * y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2D) {
            Vector2D other = (Vector2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        } else if (object instanceof Point2D) {
            Point2D other = (Point2D) object;
            return Util.fuzzyCompare(x, other.getX()) && Util.fuzzyCompare(y, other.getY());
        } else {
            return false;
        }
    }

    public Vector2D projectOnto(Vector2D other) {
        double dot = dot(other);
        double l2 = other.l2norm();
        return other.multiply(dot / l2);
    }

    public Vector2D rejectionOf(Vector2D other) {
        Vector2D projectionOntoOther = projectOnto(other);
        return this.subtract(projectionOntoOther); // Rejection
    }

    public double angleBetween(Vector2D other) {
        return angleBetween(this, other);
    }

    public static double angleBetween(Vector2D a, Vector2D b) {
        double angle = Math.atan2(a.y, a.x) - Math.atan2(b.y, b.x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }

    public Vector2D rotate(double degrees) {
        double radians = Math.toRadians(degrees);
        double rx = (this.x * Math.cos(radians)) - (this.y * Math.sin(radians));
        double ry = (this.x * Math.sin(radians)) + (this.y * Math.cos(radians));
        return new Vector2D(rx, ry);
    }

    @Override
    public String toString() {
        return String.format("Vector2D{x = %.2f, y = %.2f}", x, y);
    }

    public Point2D toPoint2D() {
        return new Point2D(x, y);
    }
}
