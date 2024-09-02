package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.ArrayList;
import java.util.List;

public abstract class CollisionResolver<T extends Collision> {
    protected Ball ball;
    protected boolean isDebugMode;
    protected List<PresentCollision> presents = new ArrayList<>();
    protected List<InevitableCollision> inevitables = new ArrayList<>();
    protected List<PotentialCollision> potentials = new ArrayList<>();

    public CollisionResolver(Ball ball, boolean isDebugMode) {
        this.ball = ball;
        this.isDebugMode = isDebugMode;
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
