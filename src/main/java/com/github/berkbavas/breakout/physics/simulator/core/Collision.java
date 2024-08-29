package com.github.berkbavas.breakout.physics.simulator.core;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.node.base.Vertex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Collision {

    //
    //
    //
    //        x  x     Contact point on ball
    //     x        x  |
    //    x   Ball   x |                Contact point on edge
    //    x    .     x.↲   --------->   .──────────┐
    //     x        x                   │          │
    //        x  x                      │ Collider │
    //                                  │          │
    //                                  └──────────┘
    //
    //

    private Collider collider;
    private ColliderEdge edge;
    private Vertex contactPointOnEdge;
    private Point2D contactPointOnBall;
    private Vector2D contactNormal;
    private double timeToCollision;
    private boolean isAlreadyColliding = false;

    public double getDistanceToCollider() {
        return contactPointOnBall.distanceTo(contactPointOnEdge);
    }
}
