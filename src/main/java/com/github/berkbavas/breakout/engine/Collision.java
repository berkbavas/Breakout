package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.ColliderEdge;
import com.github.berkbavas.breakout.engine.node.ColliderNode;
import com.github.berkbavas.breakout.math.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class Collision {
    // Object that will collide with the ball.
    private ColliderNode collider;

    // Edge of the collider that will take role in this potential collision.
    private ColliderEdge edge;

    // A collision happens when the distance between contactPointOnBall and contactPointOnEdge can be zero in the given ifps time.
    // Note that the distance between contactPointOnBall and contactPointOnEdge may be too long for a collision to happen
    // in the given time. These members are filled regardless of this case.
    private Point2D contactPointOnBall;
    private Point2D contactPointOnEdge;

    // Time needed for this potential collision to happen.
    private double timeToCollision;
}
