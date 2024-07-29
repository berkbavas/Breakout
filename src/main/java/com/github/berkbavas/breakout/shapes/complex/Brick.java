package com.github.berkbavas.breakout.shapes.complex;

import com.github.berkbavas.breakout.shapes.base.Rectangle;
import javafx.scene.paint.Color;

public class Brick extends Rectangle {
    private Color color;
    private int score;
    private boolean hit = false;
    private float radius;

    public Brick(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
