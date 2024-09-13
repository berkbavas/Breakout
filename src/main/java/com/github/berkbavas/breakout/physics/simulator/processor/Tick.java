package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.PotentialCollision;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.CuttingCriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.TangentialCriticalPoint;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
public abstract class Tick<T extends Collision> {
    private final ArrayList<T> collisions;
    private final double timeSpent;
    private final double minimumTimeToCollision;
    private final double minimumDistanceToCollision;
    private final int numberOfTangentialCriticalPoints;
    private final int numberOfCuttingCriticalPointPairs;

    @Getter
    @Setter
    private double simulationTime = 0;

    protected Tick(ArrayList<T> collisions, double timeSpent) {
        this.collisions = collisions;
        this.timeSpent = timeSpent;
        this.minimumTimeToCollision = getMinimumTimeToCollisionInner();
        this.minimumDistanceToCollision = getMinimumDistanceToCollisionInner();

        int t0 = 0;
        int t1 = 0;

        for (var collision : collisions) {
            CriticalPointPair pair = collision.getContact();

            if (pair instanceof CuttingCriticalPointPair) {
                ++t0;
            }
            if (pair instanceof TangentialCriticalPoint) {
                ++t1;
            }
        }

        numberOfTangentialCriticalPoints = t0;
        numberOfCuttingCriticalPointPairs = t1;
    }

    private double getMinimumTimeToCollisionInner() {
        var copy = new ArrayList<>(collisions);
        var sorted = copy.stream()
                .filter(collision -> collision instanceof PotentialCollision)
                .sorted((c1, c2) -> {
                    var pc1 = (PotentialCollision) c1;
                    var pc2 = (PotentialCollision) c2;

                    return Double.compare(pc1.getTimeToCollision(), pc2.getTimeToCollision());

                }).collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Double.MAX_VALUE;
        } else {
            var minimum = (PotentialCollision) sorted.get(0);
            return minimum.getTimeToCollision();
        }
    }


    private double getMinimumDistanceToCollisionInner() {
        var copy = new ArrayList<>(collisions);
        var sorted = copy.stream()
                .sorted(Comparator.comparingDouble(c -> c.getContact().getDistance())).collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Double.MAX_VALUE;
        } else {
            return sorted.get(0).getContact().getDistance();
        }
    }

    protected abstract String getChildName();

    public String toString() {
        return String.format("%s%n" +
                        "    # of Collisions             : %d%n" +
                        "    Remaining Time to Collision : %s%n" +
                        "    Time Spent                  : %.6f%n" +
                        "    Simulation Time             : %.6f%n" +
                        "    # of Tangential CP          : %d%n" +
                        "    # of Cutting CP             : %d",
                getChildName(),
                getCollisions().size(),
                minimumTimeToCollision == Double.MAX_VALUE ? "N/A" : String.format("%.6f", minimumTimeToCollision - timeSpent),
                timeSpent,
                simulationTime,
                numberOfTangentialCriticalPoints,
                numberOfCuttingCriticalPointPairs);
    }
}
