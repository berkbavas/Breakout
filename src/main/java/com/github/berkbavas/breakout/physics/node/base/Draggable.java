package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.math.Point2D;

public interface Draggable {

    boolean contains(Point2D query);

    void translate(Point2D delta);
}
