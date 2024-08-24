package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Brick extends RectangularNode implements Collider {
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
    public double getCollisionImpactFactor() {
        return Constants.Brick.COLLISION_IMPACT_FACTOR;
    }

    @Override
    public Vector2D getNormalOf(LineSegment2D edge) {
        return edge.getNormal(LineSegment2D.NormalOrientation.OUTWARDS);
    }
}
