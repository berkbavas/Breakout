package com.github.berkbavas.breakout.math;

public class Vector2D extends Point2D {

    public Vector2D() {
        super(0.0f, 0.0f);
    }

    public Vector2D(float x, float y) {
        super(x, y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D multiply(float scalar) {
        return new Vector2D(scalar * x, scalar * y);
    }

    public Vector2D opposite() {
        return this.multiply(-1.0f);
    }

    public Vector2D normalized() {
        final float norm = norm();

        float x = this.x;
        float y = this.y;

        if (!Util.fuzzyCompare(norm, 1.0f) && !Util.isFuzzyZero(norm)) {
            x = x / norm;
            y = y / norm;
        }

        return new Vector2D(x, y);
    }

    public Vector2D reflect(Vector2D normal) {
        // Reflected vector is given by the following equation.
        // r = v - 2 * (v ∙ n) * n

        normal = normal.normalized();

        final float dot = dot(this, normal);
        return this.subtract(normal.multiply(2.0f * dot));
    }

    public float dot(Vector2D other) {
        return dot(this, other);
    }

    public static float dot(Vector2D a, Vector2D b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public float norm() {
        final double xx = Math.pow(x, 2.0f);
        final double yy = Math.pow(y, 2.0f);
        return (float) Math.sqrt(xx + yy);
    }

    public float l2orm() {
        final double xx = Math.pow(x, 2.0f);
        final double yy = Math.pow(y, 2.0f);
        return (float) (xx + yy);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2D) {
            Vector2D other = (Vector2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("Vector2D{x = %.3f, y = %.3f}", x, y);
    }
}
