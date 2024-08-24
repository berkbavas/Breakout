package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class Ball extends Circle implements Draggable {
    private Vector2D velocity;
    private final Color color;

    public Ball(Point2D center, double radius, Vector2D velocity, Color color) {
        super(center, radius);
        this.velocity = velocity;
        this.color = color;
    }

    public void move(double deltaTime) {
        center = center.add(velocity.multiply(deltaTime));
    }

    public void collide(Vector2D normal) {
        velocity = velocity.reflect(normal);
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

    @Override
    public void translate(Point2D delta) {
        center = center.add(delta);
    }

    public double getSpeed() {
        return velocity.length();
    }
}
