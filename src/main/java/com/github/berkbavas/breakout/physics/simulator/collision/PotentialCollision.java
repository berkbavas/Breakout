package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PotentialCollision extends ProspectiveCollision {

    //
    //                |-------------------- Delta Time --------------------|
    //                |---------------------------- Time to Collision ---------------------------|
    //
    //        x  x
    //     x        x                                                                             ┌──────────┐
    //    x   Ball   x                                                                            │          │
    //    x    .     x  --- Velocity --->                                                         │ Collider │
    //     x        x                                                                             │          │
    //        x  x                                                                                └──────────┘
    //
    //
    //

    public PotentialCollision(Collider collider, ColliderEdge edge, SeparateCriticalPointPair contact) {
        super(collider, edge, contact);
    }
}