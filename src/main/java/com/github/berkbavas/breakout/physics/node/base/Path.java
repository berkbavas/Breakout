package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.graphics.Painter;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.List;

@Getter
public class Path implements Drawable {
    private final List<Point2D> vertices;
    private final Color color;
    private boolean isActiveDrawable = true;

    public Path(List<Point2D> vertices, Color color) {
        this.vertices = vertices;
        this.color = color;
    }

    @Override
    public boolean isActiveDrawable() {
        return isActiveDrawable;
    }

    @Override
    public void setIsActiveDrawable(boolean isActiveDrawable) {
        this.isActiveDrawable = isActiveDrawable;
    }

    @Override
    public void stroke(Painter painter, Color color, double width) {
        painter.stroke(this, color, width);
    }

    @Override
    public void stroke(Painter painter, Color color) {
        painter.stroke(this, color);
    }

    @Override
    public void stroke(Painter painter) {
        painter.stroke(this);
    }

    @Override
    public void fill(Painter painter, Color color) {
        throw new UnsupportedOperationException("Path cannot be filled!");
    }

    @Override
    public void fill(Painter painter) {
        throw new UnsupportedOperationException("Path cannot be filled!");
    }
}