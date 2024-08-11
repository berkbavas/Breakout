package com.github.berkbavas.breakout.node;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Rectangle2D;

public class World extends Rectangle2D implements GameObject {

    public World(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public World(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom) {
        super(leftTop, leftBottom, rightTop, rightBottom);
    }

}
