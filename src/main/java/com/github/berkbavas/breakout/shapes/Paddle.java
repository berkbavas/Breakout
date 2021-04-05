package com.github.berkbavas.breakout.shapes;

import com.github.berkbavas.breakout.shapes.base.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

public class Paddle extends Rectangle {
    private final Color color;
    private double radius;
    private Effect effect;

    public Paddle(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.setEffect(effect);
        gc.fillRoundRect(x, y, width, height, radius, radius);
        gc.setEffect(null);
    }
}
