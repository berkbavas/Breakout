package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.List;

@Getter
public class Obstacle extends PolygonalNode implements Draggable, Collider {

    public Obstacle(List<Point2D> vertices, List<String> identifiers, Color color) {
        super(vertices, identifiers, color);
    }

    public Obstacle(List<Point2D> vertices, Color color) {
        super(vertices, color);
    }

    @Override
    public double getCollisionImpactFactor() {
        return Constants.Obstacle.COLLISION_IMPACT_FACTOR;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }
}
