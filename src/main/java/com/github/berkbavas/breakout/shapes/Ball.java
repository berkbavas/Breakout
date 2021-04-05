package com.github.berkbavas.breakout.shapes;

import com.github.berkbavas.breakout.shapes.base.Circle;
import com.github.berkbavas.breakout.shapes.base.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends Circle {
    private final Color color;

    public Ball(double x, double y, double radius, Color color) {
        super(x, y, radius);
        this.color = color;
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public Point2D top() {
        return new Point2D(x, y - radius);
    }

    public Point2D right() {
        return new Point2D(x + radius, y);
    }

    public Point2D left() {
        return new Point2D(x - radius, y);
    }

    public Point2D bottom() {
        return new Point2D(x, y + radius);
    }

}
