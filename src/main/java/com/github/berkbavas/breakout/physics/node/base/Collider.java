package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.List;

public interface Collider extends GameObject {

    List<ColliderEdge> getEdges();

    double getRestitutionFactor();

    double getFrictionCoefficient();

    Vector2D getNormalOf(LineSegment2D edge);

    default boolean isActiveCollider() {
        return true;
    }

}
