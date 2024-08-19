package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Brick extends Rectangle2D implements StaticNode {
    private final Color color;
    @Setter
    private boolean hit;

    public Brick(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
        this.hit = false;
    }

    @Override
    public double getCollisionImpactFactor() {
        return Constants.Brick.COLLISION_IMPACT_FACTOR;
    }

}
