package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.Set;

public interface StaticNode extends GameObject {
    Set<LineSegment2D> getEdges();
    double getCollisionImpactFactor();

    default Vector2D getNormalFor(LineSegment2D edge) {
        LineSegment2D.NormalOrientation orientation;

        if (this instanceof World) {
            orientation = LineSegment2D.NormalOrientation.INWARDS;
        } else if (this instanceof Paddle || this instanceof Brick) {
            orientation = LineSegment2D.NormalOrientation.OUTWARDS;
        } else {
            throw new RuntimeException("This branch is not implemented!");
        }

        return edge.getNormal(orientation);
    }
}
