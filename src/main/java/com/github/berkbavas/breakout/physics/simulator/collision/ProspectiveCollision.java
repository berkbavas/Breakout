package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class ProspectiveCollision extends Collision {

    public ProspectiveCollision(Collider collider, ColliderEdge edge, SeparateCriticalPointPair contact) {
        super(collider, edge, contact);
    }

    @Override
    public SeparateCriticalPointPair getContact() {
        return (SeparateCriticalPointPair) super.getContact();
    }
}
