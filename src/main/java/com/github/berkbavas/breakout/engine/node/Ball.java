package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.engine.node.base.BoundingBox;
import com.github.berkbavas.breakout.engine.node.base.GameObject;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;

@Getter
public class Ball implements GameObject {
    private final Point2D center;
    private final float radius;
    private final Vector2D velocity;
    private final BoundingBox boundingBox;

    public Ball(Point2D center, float radius, Vector2D velocity) {
        this.center = center;
        this.radius = radius;
        this.velocity = velocity;
        this.boundingBox = new BoundingBox(this);
    }

    public Ball move(float time) {
        final float dx = velocity.getX() * time;
        final float dy = velocity.getY() * time;

        final Point2D newCenter = center.add(new Point2D(dx, dy));

        return new Ball(newCenter, radius, velocity);
    }

    public Ball moveThenCollide(Vector2D collisionNormal, float deltaTime) {
        final float dx = velocity.getX() * deltaTime;
        final float dy = velocity.getY() * deltaTime;

        final Point2D newCenter = center.add(new Point2D(dx, dy));
        final Vector2D newVelocity = velocity.reflect(collisionNormal);

        return new Ball(newCenter, radius, newVelocity);
    }

    public Ball collideThenMove(Vector2D collisionNormal, float deltaTime) {
        final Vector2D newVelocity = velocity.reflect(collisionNormal);
        final float dx = newVelocity.getX() * deltaTime;
        final float dy = newVelocity.getY() * deltaTime;

        final Point2D newCenter = center.add(new Point2D(dx, dy));
        return new Ball(newCenter, radius, newVelocity);
    }

    public boolean contains(Point2D point) {
        return center.distanceTo(point) <= radius;
    }

    public float distanceToCenter(Point2D point) {
        return center.distanceTo(point);
    }

    @Override
    public String toString() {
        return String.format("Ball{center = %s, radius = %.2f, velocity = %s}", center, radius, velocity);
    }

}
