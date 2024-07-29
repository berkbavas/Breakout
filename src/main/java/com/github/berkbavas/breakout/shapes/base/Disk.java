package com.github.berkbavas.breakout.shapes.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Disk {
    protected float x;
    protected float y;
    protected float radius;
    protected Vector2D velocity;

    @Setter(AccessLevel.NONE)
    private final Object lock = new Object();

    public Disk(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Object lock() {
        return lock;
    }

    @Override
    public String toString() {
        return String.format("Disk{x = %.2f, y = %.2f, radius = %.2f}", x, y, radius);
    }

    public float getLeft() {
        return x - radius;
    }

    public float getRight() {
        return x + radius;
    }

    public float getTop() {
        return y - radius;
    }

    public float getBottom() {
        return y + radius;
    }

    public void setLeft(float newLeft) {
        this.x = newLeft + radius;
    }

    public void setRight(float newRight) {
        this.x = newRight - radius;
    }

    public void setTop(float newTop) {
        this.y = newTop + radius;
    }

    public void setBottom(float newBottom) {
        this.y = newBottom - radius;
    }

    public Point2D calculateNextPosition(float ifps) {
        final float vx = velocity.getX();
        final float vy = velocity.getY();

        return new Point2D(x + vx * ifps, y + vy * ifps);
    }

    public float calculateNextLeft(float ifps) {
        return x + velocity.getX() * ifps - radius;
    }

    public float calculateNextRight(float ifps) {
        return x + velocity.getX() * ifps + radius;
    }

    public float calculateNextTop(float ifps) {
        return y + velocity.getY() * ifps - radius;
    }

    public float calculateNextBottom(float ifps) {
        return y + velocity.getY() * ifps + radius;
    }

    public float calculateNextX(float ifps) {
        return x + velocity.getX() * ifps;
    }

    public float calculateNextY(float ifps) {
        return y + velocity.getY() * ifps;
    }

    public boolean contains(Point2D point) {
        return contains(point.getX(), point.getY());
    }

    public boolean contains(float x, float y) {
        final float cx = getX();
        final float cy = getY();

        return Math.pow(x - cx, 2.0f) + Math.pow(y - cy, 2.0f) <= radius * radius;
    }

    public void reflectVelocity(Vector2D normal) {
        velocity = velocity.reflect(normal);
    }

    public void updateFrom(Disk other) {
        this.x = other.x;
        this.y = other.y;
        this.radius = other.radius;

        final float vx = other.velocity.getX();
        final float vy = other.velocity.getY();
        this.velocity = new Vector2D(vx, vy);
    }

    public void next(float ifps) {
        final float vx = velocity.getX();
        final float vy = velocity.getY();
        x = x + vx * ifps;
        y = y + vy * ifps;
    }
}
