package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PresentCollision extends Collision {

    //
    //         x   x
    //       x        x
    //      x   Ball   x
    //      x    .     x
    //       x        x
    //         x   x
    //    ‾‾‾‾‾‾‾˙‾‾‾‾‾‾‾  Collider Edge
    //        Contact

    private final CriticalPointPair contact;

    public PresentCollision(Collider collider, ColliderEdge edge, CriticalPointPair contact) {
        super(collider, edge);
        this.contact = contact;
    }
}
