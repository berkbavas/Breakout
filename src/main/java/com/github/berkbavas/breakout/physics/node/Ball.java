package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.core.Constants;
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
    private Vector2D netForce = Vector2D.ZERO;
    private boolean freeze = false;

    public Ball(Point2D center, double radius, Vector2D velocity, Color color) {
        super(center, radius, color);
        this.velocity = velocity;
    }

    public void move(double deltaTime) {
        if (freeze) {
            return;
        }


        var dx = velocity.multiply(deltaTime);
        center = center.add(dx);

        if (Util.isFuzzyZero(velocity.length())) {
            velocity = Vector2D.ZERO;
        }
    }

    public void slide(Vector2D netForce, double deltaTime) {
        if (freeze) {
            return;
        }

        double speed = velocity.length();
        double dot = netForce.dot(velocity);

        if (dot >= 0) {
            // There are three cases:
            // 1) Velocity is zero.
            // 2) Net force is zero.
            // 3) Net force and velocity has the same direction.
            // In these three cases, we call move().
            move(netForce, deltaTime);

        } else {
            // dot < 0
            // Net force and velocity has opposite direction
            double netMagnitude = netForce.length();
            double timeUntilZeroSpeed = speed / netMagnitude;

            move(netForce, Math.min(timeUntilZeroSpeed, deltaTime));
        }
    }

    public void move(Vector2D acceleration, double deltaTime) {
        if (freeze) {
            return;
        }

        var dx = velocity.multiply(deltaTime);
        center = center.add(dx);

        var dv = acceleration.multiply(deltaTime);
        velocity = velocity.add(dv);

        if (Util.isFuzzyZero(velocity.length())) {
            velocity = Vector2D.ZERO;
        }
    }

    // Reflects the velocity without considering restitution
    public void collide(Vector2D normal) {
        if (freeze) {
            return;
        }

        velocity = velocity.reflect(normal);
    }

    public void collide(Vector2D normal, double restitution, double friction) {
        if (freeze) {
            return;
        }

        // Find components
        Vector2D vertical = velocity.projectOnto(normal); // Vertical component
        Vector2D horizontal = velocity.rejectionOf(normal);  // Horizontal component

        // Restitution
        vertical = vertical.multiply(1 - restitution); // Decreased
        horizontal = horizontal.multiply(1 - friction); // Decreased

        // Clamp velocities down.
        if (vertical.length() < Constants.Ball.DO_NOT_BOUNCE_SPEED_THRESHOLD[0]) {
            vertical = Vector2D.ZERO;
        }

        if (horizontal.length() < Constants.Ball.DO_NOT_BOUNCE_SPEED_THRESHOLD[0]) {
            horizontal = Vector2D.ZERO;
        }

        velocity = vertical.reversed().add(horizontal); // Reflection is done here.

        if (Util.isFuzzyZero(velocity.length())) {
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
