package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.RectangularNode;
import javafx.scene.paint.Color;

public class World extends RectangularNode implements Collider {

    public World(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public double getFrictionCoefficient() {
        return Constants.World.FRICTION_COEFFICIENT;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.INWARDS);
    }
}
