package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class ProspectiveCollision extends Collision {
    private final SeparateCriticalPointPair contact;
    private final double timeToCollision;

    public ProspectiveCollision(Collider collider, ColliderEdge edge, SeparateCriticalPointPair contact, double timeToCollision) {
        super(collider, edge);
        this.contact = contact;
        this.timeToCollision = timeToCollision;
    }

}
