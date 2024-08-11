package com.github.berkbavas.breakout.node;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;

@Getter
public class Ball extends Circle {
    private final Vector2D velocity;

    public Ball(Point2D center, double radius, Vector2D velocity) {
        super(center, radius);
        this.velocity = velocity;
    }
}
