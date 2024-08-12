package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.StaticNode;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Collision {

    // The object subject to the collision with the ball.
    private StaticNode collider;

    // This is the edge of the collider takes role in this collision.
    private LineSegment2D edge;

    // A collision happens when the distance between
    // contactPointOnBall and contactPointOnEdge can be zero in the given ifps time.
    // Note that the distance between contactPointOnBall and contactPointOnEdge may be too long for a collision to happen
    // in the given time. These members are filled regardless of this case.
    private Point2D contactPointOnBall;
    private Point2D contactPointOnEdge;

    // Time in seconds needs to pass for this prospective collision to happen.
    private double timeToCollision;
}
