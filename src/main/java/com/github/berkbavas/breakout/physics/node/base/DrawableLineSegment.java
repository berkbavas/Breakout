package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.gui.Painter;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;

public class DrawableLineSegment extends LineSegment2D implements Drawable {
    @Getter
    private final Color color;

    private boolean isActiveDrawable = true;

    public DrawableLineSegment(Point2D P, Point2D Q, Color color) {
        super(P, Q);
        this.color = color;
    }

    public DrawableLineSegment(Point2D P, Point2D Q, String identifier, Color color) {
        super(P, Q, identifier);
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
        throw new UnsupportedOperationException("Line cannot be filled!");
    }

    @Override
    public void fill(Painter painter) {
        throw new UnsupportedOperationException("Line cannot be filled!");
    }
}
