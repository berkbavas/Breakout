package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public abstract class Collision {

    //
    //        x  x
    //     x        x                        ┌──────────┐
    //    x   Ball   x                       │          │
    //    x    .     x   --- Velocity ---→   │ Collider │
    //     x        x                        │          │
    //        x  x                         ↗ └──────────┘
    //                                   Edge
    //

    private final Collider collider;
    private final ColliderEdge edge;
    private final CriticalPointPair contact;

    public Vector2D getNormal() {
        return collider.getNormalOf(edge);
    }
}
