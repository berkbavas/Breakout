package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class CollisionResolver<T extends Collision> {
    protected Ball ball;
    protected boolean isDebugMode;

    @Getter
    protected List<T> targets = new ArrayList<>();

    public CollisionResolver(Ball ball, boolean isDebugMode) {
        this.ball = ball;
        this.isDebugMode = isDebugMode;
    }

    public abstract void load(List<Collision> collisions);

    public abstract boolean isApplicable();

    public abstract Tick<? extends Collision> resolve(double deltaTime);

    public static void sortEarliestToLatest(List<? extends ProspectiveCollision> collisions) {
        collisions.sort((c0, c1) -> {
            double ttc0 = c0.getTimeToCollision();
            double ttc1 = c1.getTimeToCollision();

            return Double.compare(ttc0, ttc1);
        });
    }

    public static ProspectiveCollision findEarliestCollision(List<? extends ProspectiveCollision> collisions) {
        List<? extends ProspectiveCollision> copy = new ArrayList<>(collisions);
        sortEarliestToLatest(copy);
        return copy.get(0);
    }

}
