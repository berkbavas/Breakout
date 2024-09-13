package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class CollisionResolver<T extends Collision> {
    protected final Set<Collider> colliders;
    protected final Ball ball;
    protected final boolean isDebugMode;
    protected final ArrayList<PresentCollision> presents = new ArrayList<>();
    protected final ArrayList<InevitableCollision> inevitables = new ArrayList<>();
    protected final ArrayList<PotentialCollision> potentials = new ArrayList<>();
    protected final NetForceCalculator netForceCalculator;

    public CollisionResolver(Set<Collider> colliders, Ball ball, boolean isDebugMode) {
        this.colliders = colliders;
        this.ball = ball;
        this.isDebugMode = isDebugMode;
        this.netForceCalculator = new NetForceCalculator(colliders);
    }

    public final void load(List<Collision> collisions) {
        presents.clear();
        inevitables.clear();
        potentials.clear();

        for (Collision collision : collisions) {
            if (collision instanceof PresentCollision) {
                presents.add((PresentCollision) collision);
            } else if (collision instanceof InevitableCollision) {
                inevitables.add((InevitableCollision) collision);
            } else if (collision instanceof PotentialCollision) {
                potentials.add((PotentialCollision) collision);
            } else {
                throw new RuntimeException("Implement this branch!");
            }
        }
    }

    public abstract boolean isApplicable();

    public abstract Tick<? extends Collision> resolve(double deltaTime);

}
