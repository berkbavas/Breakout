package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class Paddle extends RectangularNode implements Draggable, Collider {

    public Paddle(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public double getCollisionImpactFactor() {
        return Constants.Paddle.COLLISION_IMPACT_FACTOR;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }
}