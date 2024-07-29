package com.github.berkbavas.breakout.shapes.base;

public class Vector2D extends Point2D {
    private final static float EPSILON = 1e-5f;

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

    public void normalize() {
        final double xx = Math.pow(x, 2.0f);
        final double yy = Math.pow(y, 2.0f);
        final double norm = Math.sqrt(xx + yy);

        if (norm > EPSILON) {
            this.x = (float) (x / norm);
            this.y = (float) (y / norm);
        }
    }

    public Vector2D normalized() {
        Vector2D result = new Vector2D(x, y);
        result.normalize();
        return result;
    }

    public Vector2D reflect(Vector2D normal) {

        // Reflected vector is given by the following equation.
        // r = v - 2 * (v ∙ n) * n

        final float dot = dot(this, normal);
        return this.subtract(normal.multiply(2.0f * dot));
    }

    public static float dot(Vector2D a, Vector2D b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }
}
