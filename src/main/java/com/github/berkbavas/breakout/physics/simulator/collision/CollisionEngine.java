package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointFinder;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CollisionEngine {
    private final Set<Collider> colliders;
    private final Ball ball;

    public CollisionEngine(Set<Collider> colliders, Ball ball) {
        this.colliders = colliders;
        this.ball = ball;
    }

    public List<Collision> findCollisions(double deltaTime) {
        return findCollisions(colliders, ball, ball.getVelocity(), deltaTime);
    }

    public static List<Conflict> findConflicts(Set<Collider> colliders, Circle circle) {
        List<Conflict> collisions = new ArrayList<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            List<ColliderEdge> edges = collider.getEdges();

            for (ColliderEdge edge : edges) {
                CriticalPointFinder.findConflictingCriticalPoints(circle, edge)
                        .ifPresent(critical -> collisions.add(new Conflict(collider, edge, critical)));
            }
        }

        return collisions;
    }

    public static List<Collision> findCollisions(Set<Collider> colliders, Circle circle, Vector2D velocity, double deltaTime) {
        CollisionConstructor ctor = new CollisionConstructor(circle, velocity, deltaTime);
        List<Collision> collisions = new ArrayList<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            List<ColliderEdge> edges = collider.getEdges();

            for (ColliderEdge edge : edges) {
                CriticalPointFinder.findCriticalPointsAlongGivenDirection(circle, edge, velocity)
                        .flatMap(critical -> ctor.constructIfPossible(collider, edge, critical))
                        .ifPresent(collisions::add);
            }
        }

        return collisions;
    }


    //
    //        x  x -----Direction----->
    //     x        x
    //    x  Circle  x
    //    x    .     x.↲
    //     x        x                  ↳.┌──────────┐
    //        x  x -----Direction----->  │          │
    //                                   │ Collider │
    //                                   │          │
    //                                   └──────────┘
    //
    // Finds the set of pairs closest each other among all points on the collider and circle along the given direction.
    // The result is a set rather than a single element because there may be two points on the collider,
    // such as the common vertex of two edges, closest to circle.
    // Two dots, one on the circle the other on the rectangle,
    // in the image above are the closest pair along the direction vector.

    public static List<CriticalPointPair> findCriticalPointsAlongGivenDirection(Circle circle, Collider collider, Vector2D direction) {
        List<CriticalPointPair> result = new ArrayList<>();
        List<ColliderEdge> edges = collider.getEdges();
        Point2D center = circle.getCenter();

        for (ColliderEdge edge : edges) {
            CriticalPointFinder.findCriticalPointsAlongGivenDirection(circle, edge, direction).ifPresent(critical -> {
                if (CollisionConstructor.isPointWithinCollisionTrajectory(center, critical.getPointOnEdge(), direction)) {
                    result.add(critical);
                }
            });
        }

        return result;
    }

    public static Optional<CriticalPointPair> findMostCriticalPointAlongGivenDirection(Circle circle, Collider collider, Vector2D direction) {
        var criticalPoints = findCriticalPointsAlongGivenDirection(circle, collider, direction);

        // Sort closest to farthest
        criticalPoints.sort((p0, p1) -> {
            double d0 = p0.getDistance();
            double d1 = p1.getDistance();

            return Double.compare(d0, d1);
        });

        return criticalPoints.isEmpty() ? Optional.empty() : Optional.of(criticalPoints.get(0));
    }

    public static void sortEarliestToLatest(List<? extends ProspectiveCollision> collisions) {
        collisions.sort((c0, c1) -> {
            double ttc0 = c0.getTimeToCollision();
            double ttc1 = c1.getTimeToCollision();

            return Double.compare(ttc0, ttc1);
        });
    }

    public static ProspectiveCollision findEarliestCollision(List<? extends ProspectiveCollision> collisions) {
        if (collisions.isEmpty()) {
            throw new IllegalArgumentException("collisions is empty!");
        }

        List<? extends ProspectiveCollision> copy = new ArrayList<>(collisions);
        sortEarliestToLatest(copy);
        return copy.get(0);
    }

    public static Vector2D calculateCollectiveCollisionNormal(List<? extends ProspectiveCollision> collisions, Vector2D velocity) {
        if (velocity.l2norm() == 0) {
            throw new IllegalArgumentException("velocity must be non-zero vector!");
        }

        Vector2D result = new Vector2D(0, 0);

        for (ProspectiveCollision collision : collisions) {
            Vector2D normal = collision.getNormal();

            // We only consider normals whose dot product with the velocity vector is negative.
            // Otherwise, reflection of the velocity vector w.r.t. collision normal does not make any sense.
            if (normal.dot(velocity) < -Util.EPSILON) {
                result = result.add(normal);
            }
        }

        return result.normalized();
    }
}
