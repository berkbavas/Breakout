package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public abstract class Tick<T extends Collision> {
    private final List<T> collisions;
    private final double timeSpent;

    public double getMinimumTimeToCollision() {
        double minimum = Double.MAX_VALUE;
        for (var collision : collisions) {
            if (collision instanceof ProspectiveCollision) {
                double timeToCollision = ((ProspectiveCollision) collision).getTimeToCollision();
                if (timeToCollision < minimum) {
                    minimum = timeToCollision;
                }
            }
        }
        return minimum;
    }
}
