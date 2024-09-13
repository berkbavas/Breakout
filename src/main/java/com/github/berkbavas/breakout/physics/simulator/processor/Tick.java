package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public abstract class Tick<T extends Collision> {
    private final ArrayList<T> collisions;
    private final double timeSpent;
    private final double minimumTimeToCollision;

    @Getter
    @Setter
    private double simulationTime = 0;

    protected Tick(ArrayList<T> collisions, double timeSpent) {
        this.collisions = collisions;
        this.timeSpent = timeSpent;
        this.minimumTimeToCollision = getMinimumTimeToCollision();
    }

    public double getMinimumTimeToCollision() {
        double minimum = Double.MAX_VALUE;

        if (collisions != null) {
            for (var collision : collisions) {
                if (collision instanceof ProspectiveCollision) {
                    double timeToCollision = ((ProspectiveCollision) collision).getTimeToCollision();
                    if (timeToCollision < minimum) {
                        minimum = timeToCollision;
                    }
                }
            }
        }
        return minimum;
    }

    protected abstract String getChildName();

    public String toString() {
        return String.format("%s%n" +
                        "    # of Collisions           : %d%n" +
                        "    Minimum Time to Collision : %s%n" +
                        "    Time Spent                : %.6f%n" +
                        "    Simulation Time           : %.6f",
                getChildName(),
                getCollisions().size(),
                minimumTimeToCollision == Double.MAX_VALUE ? "N/A" : String.format("%.6f", minimumTimeToCollision),
                timeSpent,
                simulationTime);
    }
}
