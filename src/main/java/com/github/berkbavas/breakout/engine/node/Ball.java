package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Rectangle2D;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;

@Getter
public class Ball extends Circle implements GameObject {
    private final Vector2D velocity;
    private final Rectangle2D boundingBox;

    public Ball(Point2D center, double radius, Vector2D velocity) {
        super(center, radius);
        this.velocity = velocity;
        double x = center.getX() - radius;
        double y = center.getY() - radius;
        boundingBox = new Rectangle2D(x, y, 2 * radius, 2 * radius);
    }

    public Ball move(double deltaTime) {
        final double dx = velocity.getX() * deltaTime;
        final double dy = velocity.getY() * deltaTime;

        Point2D newCenter = getCenter().add(new Point2D(dx, dy));
        return new Ball(newCenter, getRadius(), velocity);
    }

    public Ball collide(Vector2D collisionNormal, double timeToCollision) {
        final double dx = velocity.getX() * timeToCollision;
        final double dy = velocity.getY() * timeToCollision;
        Vector2D newVelocity = velocity.reflect(collisionNormal);
        Point2D newCenter = getCenter().add(new Point2D(dx, dy));
        return new Ball(newCenter, getRadius(), newVelocity);
    }

    public Ball collide(Vector2D collisionNormal, double timeToCollision, double timeAfterCollision) {
        double dx = velocity.getX() * timeToCollision;
        double dy = velocity.getY() * timeToCollision;

        Vector2D newVelocity = velocity.reflect(collisionNormal);

        dx += newVelocity.getX() * timeAfterCollision;
        dy += newVelocity.getY() * timeAfterCollision;

        Point2D newCenter = getCenter().add(new Point2D(dx, dy));

        return new Ball(newCenter, getRadius(), newVelocity);
    }

    public Ball translateAlongDirection(Vector2D direction, double distance) {
        Point2D newCenter = getCenter().add(direction.multiply(distance));
        return new Ball(newCenter, getRadius(), getVelocity());
    }
}
