package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.math.Line2D;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ColliderEdge extends LineSegment2D {
    // The line passing through this line segment
    // We store this member for the sake of performant intersection calculations.
    private final Line2D line = Line2D.from(this);

    public ColliderEdge(Point2D P, Point2D Q, String identifier) {
        super(P, Q, identifier);
    }

    public ColliderEdge(Point2D P, Point2D Q) {
        super(P, Q);
    }
}
