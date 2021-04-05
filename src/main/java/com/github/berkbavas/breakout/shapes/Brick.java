package com.github.berkbavas.breakout.shapes;

import com.github.berkbavas.breakout.shapes.base.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends Rectangle {
    private final Color color;
    private int score;
    private boolean hit;
    private double radius;

    public Brick(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
        hit = false;
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, width, height, radius, radius);
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
