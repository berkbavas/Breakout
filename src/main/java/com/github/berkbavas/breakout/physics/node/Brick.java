package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Drawable;
import com.github.berkbavas.breakout.physics.node.base.RectangularNode;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Brick extends RectangularNode implements Collider, Drawable {
    private boolean hit;

    public Brick(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
        this.hit = false;
    }

    @Override
    public boolean isActiveCollider() {
        return !hit;
    }

    @Override
    public boolean isActiveDrawable() {
        return !hit;
    }

    @Override
    public double getImpulsionFactor() {
        return Constants.Brick.IMPULSION_FACTOR.getValue();
    }

    @Override
    public double getRestitutionFactor() {
        return Constants.Brick.RESTITUTION_FACTOR.getValue();
    }

    @Override
    public double getFrictionCoefficient() {
        return Constants.Brick.FRICTION_COEFFICIENT.getValue();
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }

}
