package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.graphics.Painter;
import com.github.berkbavas.breakout.math.AbstractRectangle2D;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;


public abstract class RectangularNode extends AbstractRectangle2D<ColliderEdge> implements Drawable {
    @Getter
    private final Color color;
    private boolean isActiveDrawable = true;

    public RectangularNode(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    protected ColliderEdge createEdge(Point2D P, Point2D Q, String identifier) {
        return new ColliderEdge(P, Q, identifier);
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
