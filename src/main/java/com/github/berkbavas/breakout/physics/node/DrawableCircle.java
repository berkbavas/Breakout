package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.graphics.Painter;
import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;

public class DrawableCircle extends Circle implements Drawable {
    @Getter
    private final Color color;
    private boolean isActiveDrawable = true;

    public DrawableCircle(Point2D center, double radius, Color color) {
        super(center, radius);
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
        painter.fill(this, color);
    }

    @Override
    public void fill(Painter painter) {
        painter.fill(this);
    }
}
