package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.DrawableCircle;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Ball extends DrawableCircle implements Draggable {
    private Vector2D velocity;
    private Vector2D acceleration = new Vector2D(0, 0);

    public Ball(Point2D center, double radius, Vector2D velocity, Color color) {
        super(center, radius, color);
        this.velocity = velocity;
    }

    public void move(double deltaTime) {
        center = getNextCenterPosition(deltaTime);
        velocity = getNextVelocity(deltaTime);
    }

    public Point2D getNextCenterPosition(double deltaTime) {
        // v(t) = v + at
        // x(t) = vt + 0.5at^2
        //      = x0   + x1
        Vector2D x0 = velocity.multiply(deltaTime);
        Vector2D x1 = acceleration.multiply(deltaTime * deltaTime).multiply(0.5);
        return center.add(x0.add(x1));
    }

    public Vector2D getAverageVelocity(double deltaTime) {
        // v(t) = v + at
        // x(t) = vt + 0.5at^2

        // averageVelocity = (x(0) - x(deltaTime)) / deltaTime
        //                 = v + 0.5 * a * deltaTime.

        return velocity.add(acceleration.multiply(deltaTime).multiply(0.5));
    }

    public Vector2D getNextVelocity(double deltaTime) {
        // v(t) = v + at
        return velocity.add(acceleration.multiply(deltaTime));
    }

    public void collide(Vector2D normal, double dampingFactor) {
        double dampingStrength = Util.clamp(0, -Vector2D.dot(velocity.normalized(), normal), 1);
        velocity = velocity.multiply(1 - dampingStrength * dampingFactor).reflect(normal);
    }

    public void translate(Vector2D direction, double distance) {
        center = center.add(direction.multiply(distance));
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    @Override
    public boolean contains(Point2D query) {
        Circle dummy = new Circle(center, 4 * radius);
        return dummy.isPointInsideCircle(query);
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

    public Ball deepCopy() {
        Ball ball = new Ball(center, radius, velocity, getColor());
        ball.setAcceleration(acceleration);
        return ball;
    }

}
