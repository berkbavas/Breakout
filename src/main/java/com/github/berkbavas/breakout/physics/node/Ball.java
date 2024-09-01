package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
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
    private Vector2D acceleration = new Vector2D(0, 0);

    public Ball(Point2D center, double radius, Vector2D velocity, Color color) {
        super(center, radius, color);
        this.velocity = velocity;
    }

    public void move(double deltaTime) {
        center = center.add(velocity.multiply(deltaTime));
        velocity = velocity.add(acceleration.multiply(deltaTime));
    }

    // Reflects the velocity by ignoring restitution
    public void collide(Vector2D normal) {
        velocity = velocity.reflect(normal);
    }

    public void collide(Vector2D normal, double restitutionFactor) {
        Vector2D vertical = velocity.projectOnto(normal); // Vertical component
        Vector2D horizontal = velocity.rejectionOf(normal);  // Horizontal component
        Vector2D verticalAfterCollision = vertical.multiply(1 - restitutionFactor).reversed();
        if (verticalAfterCollision.length() <= Constants.Ball.DO_NOT_REFLECT_VELOCITY_THRESHOLD.getValue()) {
            velocity = horizontal;
        } else {
            velocity = verticalAfterCollision.add(horizontal);
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

    public double getSpeed() {
        return velocity.length();
    }

    public double getScalarAcceleration() {
        return acceleration.length();
    }

    public boolean isSteady() {
        return Util.isFuzzyZero(getSpeed()) && Util.isFuzzyZero(getScalarAcceleration());
    }

    public Ball copy() {
        Ball ball = new Ball(center, radius, velocity, getColor());
        ball.setAcceleration(acceleration);
        return ball;
    }

}
