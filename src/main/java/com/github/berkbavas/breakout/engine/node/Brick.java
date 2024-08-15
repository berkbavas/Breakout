package com.github.berkbavas.breakout.engine.node;

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

    public Brick(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom, Color color) {
        super(leftTop, leftBottom, rightTop, rightBottom);
        this.color = color;
        this.hit = false;
    }
}
