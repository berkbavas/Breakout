package com.github.berkbavas.breakout.physics.simulator.core;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.physics.node.base.Vertex;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CollisionEngine {
    private final Set<Collider> colliders;
    private final Ball ball;

    public CollisionEngine(Set<Collider> colliders, Ball ball) {
        this.colliders = colliders;
        this.ball = ball;
    }

    public Set<Collision> filterEarliestCollisions(double deltaTime) {
        Vector2D velocity = ball.getNextVelocity(deltaTime);
        return findEarliestCollisions(colliders, ball, velocity);
    }

    public Set<Collision> findCollisions(double deltaTime) {
        Vector2D velocity = ball.getNextVelocity(deltaTime);
        return findCollisions(colliders, ball, velocity);
    }

    // Returns the pair of distance to the collider and the normal of the collider.
    public Optional<Pair<Double, Vector2D>> findClosestColliderAlongGivenDirection(Vector2D direction) {
        return findClosestColliderAlongGivenDirection(colliders, ball, direction);
    }

    public static Set<Collision> findEarliestCollisions(Set<Collider> colliders, Circle circle, Vector2D velocity) {
        Set<Collision> collisions = findCollisions(colliders, circle, velocity);
        return filterEarliestCollisions(collisions);
    }

    public static Set<Collision> findCollisions(Set<Collider> colliders, Circle circle, Vector2D velocity) {

        Set<Collision> collisions = new HashSet<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            Set<Pair<Point2D, Vertex>> pairs = findClosestPairsAlongGivenDirection(circle, collider, velocity);

            for (Pair<Point2D, Vertex> pair : pairs) {
                Point2D contactPointOnBall = pair.getKey();
                Vertex contactPointOnEdge = pair.getValue();
                Vector2D normal = collider.getNormalOf(contactPointOnEdge.getOwner());

                double distance = contactPointOnBall.distanceTo(contactPointOnEdge);
                double speed = velocity.length();
                double time = Util.isFuzzyZero(speed) ? Double.POSITIVE_INFINITY : distance / speed;

                boolean isAlreadyColliding = Util.isFuzzyZero(distance);

                Collision collision = new Collision();
                collision.setCollider(collider);
                collision.setEdge(contactPointOnEdge.getOwner());
                collision.setContactPointOnEdge(contactPointOnEdge);
                collision.setContactPointOnBall(contactPointOnBall);
                collision.setContactNormal(normal);
                collision.setTimeToCollision(time);
                collision.setAlreadyColliding(isAlreadyColliding);
                collisions.add(collision);
            }
        }

        return collisions;
    }

    public static Optional<Collision> findEarliestCollision(Set<Collider> colliders, Ball ball, Vector2D velocity) {
        Set<Collision> collisions = findEarliestCollisions(colliders, ball, velocity);
        return collisions.stream().findFirst();
    }

    public static Set<Pair<Collider, Vertex>> findIntersections(Set<Collider> colliders, Circle circle) {
        Set<Pair<Collider, Vertex>> result = new HashSet<>();

        for (Collider collider : colliders) {
            if (!collider.isActiveCollider()) {
                continue;
            }

            List<ColliderEdge> edges = collider.getEdges();

            for (ColliderEdge edge : edges) {
                Set<Vertex> intersections = edge.findIntersection(circle);

                for (Vertex intersection : intersections) {
                    result.add(new Pair<>(collider, intersection));
                }
            }
        }

        return result;
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

    public static Set<Pair<Point2D, Vertex>> findClosestPairsAlongGivenDirection(Circle circle, Collider collider, Vector2D direction) {
        Set<Pair<Point2D, Vertex>> pairs = new HashSet<>();

        List<ColliderEdge> edges = collider.getEdges();
        final Point2D center = circle.getCenter();

        for (ColliderEdge edge : edges) {
            Pair<Point2D, Vertex> pair = edge.findClosestPairAlongGivenDirection(circle, direction).orElse(null);

            if (pair == null) {
                continue;
            }

            Point2D pointOnCircle = pair.getKey();
            Vertex pointOnEdge = pair.getValue();

            if (isPointWithinCollisionTrajectory(center, direction, pointOnEdge)) {
                // If we are here, this is a potential collision
                pairs.add(new Pair<>(pointOnCircle, pointOnEdge));
            }
        }

        return pairs;
    }

    // Returns the pair of distance to the collider and the normal of the collider.
    public static Optional<Pair<Double, Vector2D>> findClosestColliderAlongGivenDirection(Set<Collider> colliders, Circle circle, Vector2D direction) {
        double minDistance = Double.MAX_VALUE;
        Vector2D normal = null;

        for (Collider collider : colliders) {
            Set<Pair<Point2D, Vertex>> pairs = findClosestPairsAlongGivenDirection(circle, collider, direction);

            for (Pair<Point2D, Vertex> pair : pairs) {
                Point2D pointOnCircle = pair.getKey();
                Vertex pointOnEdge = pair.getValue();

                double distance = Point2D.distanceBetween(pointOnCircle, pointOnEdge);
                if (distance < minDistance) {
                    minDistance = distance;
                    normal = collider.getNormalOf(pointOnEdge.getOwner());
                }
            }
        }

        if (normal != null) {
            return Optional.of(new Pair<>(minDistance, normal));
        }

        return Optional.empty();
    }

    public static Vector2D calculateCollectiveCollisionNormal(Set<Collision> collisions, Vector2D velocity) {
        Vector2D result = new Vector2D(0, 0);

        for (Collision collision : collisions) {
            Vector2D normal = collision.getContactNormal();

            double dot = normal.dot(velocity);
            // We only consider normals whose dot product with the velocity vector is negative.
            // Otherwise, reflection of the velocity vector w.r.t. collision normal does not make any sense.
            if (dot < 0.0) {
                result = result.add(normal);
            }
        }

        return result.normalized();
    }

    public static Set<Collision> filterEarliestCollisions(Set<Collision> collisions) {
        final double minimumTimeToCollision = findMinimumTimeToCollision(collisions);

        return collisions.stream().filter(collision -> collision.getTimeToCollision() <= minimumTimeToCollision).collect(Collectors.toSet());
    }

    public static double findMinimumTimeToCollision(Set<Collision> collisions) {
        double minTimeToCollision = Double.MAX_VALUE;

        for (Collision collision : collisions) {
            final double timeToCollision = collision.getTimeToCollision();
            if (timeToCollision <= minTimeToCollision) {
                minTimeToCollision = timeToCollision;
            }
        }

        return minTimeToCollision;
    }

    private static boolean isPointWithinCollisionTrajectory(Point2D pointOnMovingObject, Vector2D velocity, Point2D point) {
        Vector2D movingObjectToPoint = point.subtract(pointOnMovingObject);
        double dot = Vector2D.dot(movingObjectToPoint, velocity);
        return dot > 0;
    }
}
