package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.DrawableCircle;
import com.github.berkbavas.breakout.physics.node.base.GameObject;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Ball extends DrawableCircle implements Draggable, GameObject {
    private Vector2D velocity;
    private Vector2D pull = Vector2D.ZERO;
    private Vector2D resistance = Vector2D.ZERO;

    public Ball(Point2D center, double radius, Vector2D velocity, Color color) {
        super(center, radius, color);
        this.velocity = velocity;

    }

    public void move(double deltaTime) {
        var deltaPosition = velocity.multiply(deltaTime);
        center = center.add(deltaPosition);
    }

    public void applyPull(Vector2D push, double deltaTime) {
        velocity = velocity.add(push.multiply(deltaTime));
    }

    public void slide(double deltaTime, Vector2D pull, Vector2D resistance) {
        Vector2D net = pull.add(resistance);
        double speed = velocity.length();
        double dot = net.dot(velocity);

        if (dot > 0 || dot == 0) {
            // There are three cases:
            // 1) Velocity is zero.
            // 2) Net force is zero.
            // 3) Net force and velocity has the same direction.
            // In these three cases, we call move().
            move(deltaTime, net);

        } else {
            // dot < 0
            // Net force and velocity has opposite direction
            double netMagnitude = net.length();
            double timeUntilZeroSpeed = speed / netMagnitude;

            move(Math.min(timeUntilZeroSpeed, deltaTime), net);
        }
    }

    public void move(double deltaTime, Vector2D acceleration) {
        // c = c + v*t + 0.5*a*t^2
        center = center.add(velocity.multiply(deltaTime)).add(acceleration.multiply(0.5 * deltaTime * deltaTime));
        velocity = velocity.add(acceleration.multiply(deltaTime));
    }

    // Reflects the velocity without considering restitution
    public void collide(Vector2D normal) {
        velocity = velocity.reflect(normal);
    }

    public void collide(Vector2D normal, double restitution, double friction) {
        double angle = Vector2D.angleBetween(normal, velocity);
        double normalizedAngle = Math.abs(angle) - 90;

        Vector2D vertical = velocity.projectOnto(normal); // Vertical component
        Vector2D horizontal = velocity.rejectionOf(normal);  // Horizontal component

        Vector2D verticalReduced = vertical.multiply(1 - restitution);
        Vector2D horizontalReduced = horizontal.multiply(1 - friction);

        Vector2D calculated = verticalReduced.reversed().add(horizontalReduced);

        if (normalizedAngle < Constants.Ball.DO_NOT_REFLECT_ANGLE_THRESHOLD) {
            velocity = horizontalReduced;
        } else if (calculated.length() > Constants.Ball.DO_NOT_BOUNCE_SPEED_THRESHOLD) {
            velocity = calculated;
        } else {
            velocity = Vector2D.ZERO;
        }
    }

    public void translate(Vector2D direction, double distance) {
        center = center.add(direction.multiply(distance));
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    @Override
    public boolean contains(Point2D query) {
        return isPointInsideCircle(query);
    }

    public boolean contains(Point2D query, double tolerance) {
        double dx = center.getX() - query.getX();
        double dy = center.getY() - query.getY();
        double distance = dx * dx + dy * dy;
        double maxDistance = tolerance * tolerance * radius * radius;
        return Util.isBetween(0.0, distance, maxDistance);
    }

    @Override
    public void translate(Point2D delta) {
        center = center.add(delta);
    }

    public Ball copy() {
        return new Ball(center, radius, velocity, getColor());
    }

    @Override
    public Circle enlarge(double factor) {
        return super.enlarge(factor);
    }

    public double getSpeed() {
        return velocity.length();
    }

    public boolean isStationary() {
        return Util.isFuzzyZero(getSpeed());
    }

}
