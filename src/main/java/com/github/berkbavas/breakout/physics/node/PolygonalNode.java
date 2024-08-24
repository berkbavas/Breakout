package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.math.AbstractPolygon2D;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class PolygonalNode extends AbstractPolygon2D<ColliderEdge> {
    private final Color color;

    public PolygonalNode(List<Point2D> vertices, List<String> identifiers, Color color) {
        super(vertices, identifiers);
        this.color = color;
    }

    public PolygonalNode(List<Point2D> vertices, Color color) {
        super(vertices);
        this.color = color;
    }

    @Override
    protected ColliderEdge createEdge(Point2D P, Point2D Q, String identifier) {
        return new ColliderEdge(P, Q, identifier);
    }

}
