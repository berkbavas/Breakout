package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.math.AbstractRectangle2D;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public abstract class RectangularNode extends AbstractRectangle2D<ColliderEdge> {
    private final Color color;

    public RectangularNode(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    protected ColliderEdge createEdge(Point2D P, Point2D Q, String identifier) {
        return new ColliderEdge(P, Q, identifier);
    }
}
