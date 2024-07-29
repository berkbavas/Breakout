package com.github.berkbavas.breakout.shapes.complex;

import com.github.berkbavas.breakout.shapes.base.Disk;

import javafx.scene.paint.Color;

public class Ball extends Disk {
    private final Color color;

    public Ball(float x, float y, float radius, Color color) {
        super(x, y, radius);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
