package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.PolygonalNode;
import javafx.scene.paint.Color;
import lombok.Setter;

import java.util.List;

@Setter
public class Obstacle extends PolygonalNode implements Draggable, Collider {

    public Obstacle(List<Point2D> vertices, List<String> identifiers, Color color) {
        super(vertices, identifiers, color);
    }

    public Obstacle(List<Point2D> vertices, Color color) {
        super(vertices, color);
    }

    public Obstacle(double x, double y, double width, double height, Color color) {
        this(List.of(
                new Point2D(x, y),
                new Point2D(x, y + height),
                new Point2D(x + width, y + height),
                new Point2D(x + width, y)), color);
    }

    @Override
    public double getFrictionCoefficient() {
        return Constants.Obstacle.FRICTION_COEFFICIENT[0];
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }

}
