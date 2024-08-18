package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.math.LineSegment2D;

import java.util.Set;

public interface StaticNode extends GameObject {
    Set<LineSegment2D> getEdges();
    double getCollisionImpactFactor();
}
