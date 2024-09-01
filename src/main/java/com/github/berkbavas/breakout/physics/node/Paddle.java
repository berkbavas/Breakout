package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.RectangularNode;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Paddle extends RectangularNode implements Draggable, Collider {
    private boolean isActiveCollider = true;

    public Paddle(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public double getRestitutionFactor() {
        return Constants.Paddle.RESTITUTION_FACTOR.getValue();
    }

    @Override
    public double getFrictionCoefficient() {
        return Constants.Paddle.FRICTION_COEFFICIENT.getValue();
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }
}
