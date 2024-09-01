package com.github.berkbavas.breakout.physics.simulator.collision;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointFinder;
import com.github.berkbavas.breakout.physics.simulator.helper.CriticalPointPair;
import com.github.berkbavas.breakout.physics.simulator.helper.SeparateCriticalPointPair;

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

    public List<PresentCollision> findPresentCollisions() {
        return findPresentCollisions(colliders, ball);
    }

    public static List<PresentCollision> findPresentCollisions(Set<Collider> colliders, Circle circle) {
        List<PresentCollision> collisions = new ArrayList<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            List<ColliderEdge> edges = collider.getEdges();

            for (ColliderEdge edge : edges) {
                CriticalPointFinder.findConflictingCriticalPoints(circle, edge).ifPresent(critical -> collisions.add(new PresentCollision(collider, edge, critical)));
            }
        }

        return collisions;
    }

    public static List<Collision> findCollisions(Set<Collider> colliders, Circle circle, Vector2D velocity, double deltaTime) {
        final double speed = velocity.length();
        final Point2D center = circle.getCenter();
        List<Collision> collisions = new ArrayList<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            List<ColliderEdge> edges = collider.getEdges();
            for (ColliderEdge edge : edges) {
                CriticalPointFinder.findCriticalPointsAlongGivenDirection(circle, edge, velocity).ifPresent(critical -> {

                    if (critical instanceof SeparateCriticalPointPair) {
                        SeparateCriticalPointPair separate = (SeparateCriticalPointPair) critical;
                        Point2D pointOnCircle = separate.getPointOnCircle();
                        Point2D pointOnEdge = separate.getPointOnEdge();

                        if (isPointWithinCollisionTrajectory(center, velocity, pointOnEdge)) {
                            double distance = Point2D.distanceBetween(pointOnEdge, pointOnCircle);
                            double timeToCollision = distance / speed;
                            boolean isInevitableCollision = timeToCollision <= deltaTime;

                            if (isInevitableCollision) {
                                collisions.add(new InevitableCollision(collider, edge, separate, timeToCollision));
                            } else {
                                collisions.add(new PotentialCollision(collider, edge, separate, timeToCollision));
                            }
                        }

                    } else {
                        collisions.add(new PresentCollision(collider, edge, critical));
                    }


                });

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
    // Two dots, one on the circle the other on the rectangle, in the image above are the closest pair along the direction vector.
    // In fact this is an edge case where the dot on the rectangle is the common vertex of two edges of the rectangle,
    // so we return a set in this ase {(pointOnCircle, pointOnEdge0), (pointOnCircle, pointOnEdge1)},
    // where pointOnEdge0 and pointOnEdge1 represent the same point in the plane, but they belong to different edges.
    // The owner of a vertex is crucial because we need to know which edges take part in a collision in order to
    // calculate the 'collective' collision normal.

    public static List<CriticalPointPair> findCriticalPointsAlongGivenDirection(Circle circle, Collider collider, Vector2D direction) {
        List<CriticalPointPair> result = new ArrayList<>();

        List<ColliderEdge> edges = collider.getEdges();
        Point2D center = circle.getCenter();

        for (ColliderEdge edge : edges) {
            CriticalPointFinder.findCriticalPointsAlongGivenDirection(circle, edge, direction).ifPresent(critical -> {

                if (isPointWithinCollisionTrajectory(center, direction, critical.getPointOnEdge())) {
                    result.add(critical);
                }
            });
        }

        return result;
    }

    public static Optional<CriticalPointPair> findMostCriticalPointAlongGivenDirection(Circle circle, Collider collider, Vector2D direction) {
        CriticalPointPair result = null;
        var criticalPoints = findCriticalPointsAlongGivenDirection(circle, collider, direction);
        double minDistance = Double.MAX_VALUE;
        for (var criticalPoint : criticalPoints) {
            double distance = criticalPoint.getDistance();
            if (distance < minDistance) {
                minDistance = distance;
                result = criticalPoint;
            }
        }
        return Optional.ofNullable(result);
    }

    private static boolean isPointWithinCollisionTrajectory(Point2D pointOnMovingObject, Vector2D velocity, Point2D point) {
        Vector2D movingObjectToPoint = point.subtract(pointOnMovingObject);
        double dot = Vector2D.dot(movingObjectToPoint, velocity);
        return dot > 0;
    }
}
