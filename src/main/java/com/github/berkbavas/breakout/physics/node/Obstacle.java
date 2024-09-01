package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
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


    @Override
    public double getRestitutionFactor() {
        return Constants.Obstacle.RESTITUTION_FACTOR.getValue();
    }

    @Override
    public double getFrictionCoefficient() {
        return Constants.Obstacle.FRICTION_COEFFICIENT.getValue();
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }

}
