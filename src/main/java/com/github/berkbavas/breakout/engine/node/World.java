package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;

public class World extends RectangularNode {

    public World(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public double getCollisionImpactFactor() {
        return Constants.World.COLLISION_IMPACT_FACTOR;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.INWARDS);
    }
}
