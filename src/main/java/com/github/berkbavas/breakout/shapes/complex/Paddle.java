package com.github.berkbavas.breakout.shapes.complex;

import com.github.berkbavas.breakout.shapes.base.Rectangle;
import javafx.scene.paint.Color;

public class Paddle extends Rectangle {
    private Color color;
    private float radius;

    public Paddle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
