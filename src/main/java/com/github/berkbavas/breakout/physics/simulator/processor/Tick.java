package com.github.berkbavas.breakout.physics.simulator.processor;

import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.CuttingCriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;
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
    private final int numberOfSeparateCriticalPointPairs;
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
        int t2 = 0;

        for (var collision : collisions) {
            CriticalPointPair pair = collision.getContact();

            if (pair instanceof SeparateCriticalPointPair) {
                ++t0;
            }
            if (pair instanceof CuttingCriticalPointPair) {
                ++t1;
            }
            if (pair instanceof TangentialCriticalPoint) {
                ++t2;
            }
        }

        numberOfSeparateCriticalPointPairs = t0;
        numberOfTangentialCriticalPoints = t1;
        numberOfCuttingCriticalPointPairs = t2;
    }

    private double getMinimumTimeToCollisionInner() {
        var copy = new ArrayList<>(collisions);
        var sorted = copy.stream()
                .filter(collision -> collision instanceof InevitableCollision)
                .sorted((c1, c2) -> {
                    var pc1 = (InevitableCollision) c1;
                    var pc2 = (InevitableCollision) c2;

                    return Double.compare(pc1.getTimeToCollision(), pc2.getTimeToCollision());

                }).collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Double.MAX_VALUE;
        } else {
            var minimum = (InevitableCollision) sorted.get(0);
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
                        "    # of Separate CPs           : %d%n" +
                        "    # of Tangential CPs         : %d%n" +
                        "    # of Cutting CPs            : %d",
                getChildName(),
                getCollisions().size(),
                minimumTimeToCollision == Double.MAX_VALUE ? "N/A" : String.format("%.6f", minimumTimeToCollision - timeSpent),
                timeSpent,
                simulationTime,
                numberOfSeparateCriticalPointPairs,
                numberOfTangentialCriticalPoints,
                numberOfCuttingCriticalPointPairs);
    }
}
