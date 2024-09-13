package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import lombok.Getter;

@Getter
public class Conflict extends Collision {

    // This class represents two cases below.
    //
    //         x   x
    //       x        x
    //      x   Ball   x
    //      x    .     x
    //       x        x
    //         x   x
    //    ‾‾‾‾‾‾‾˙‾‾‾‾‾‾‾  Collider Edge
    //
    // or
    //
    //         x   x
    //       x        x
    //      x   Ball   x
    //      x    .     x
    //  ----.---------.----- Collider Edge
    //         x   x
    //

    // PresentCollision is special case of a Conflict where the dot product of velocity and collider normal is negative,
    // in other words, Conflict is velocity ignorant while PresentCollision concerns the direction of velocity and
    // the normal of the collider.

    public Conflict(Collider collider, ColliderEdge edge, CriticalPointPair contact) {
        super(collider, edge, contact);
    }

}
