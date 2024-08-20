package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.engine.Collision;
import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ColliderNode extends GameObject {

    List<ColliderEdge> getEdges();

    double getCollisionImpactFactor();

    Vector2D getNormalOf(LineSegment2D edge);

    default Set<Collision> findCollisions(Circle circle, Vector2D velocity) {
        Set<Collision> collisions = new HashSet<>();

        List<ColliderEdge> edges = getEdges();

        for (ColliderEdge edge : edges) {
            Pair<Point2D, Point2D> pair = edge.findClosestPairAlongGivenDirection(circle, velocity).orElse(null);
            if (pair == null) {
                continue;
            }

            Point2D contactPointOnCircle = pair.getKey();
            Point2D contactPointOnEdge = pair.getValue();

            if (isPointWithinCollisionTrajectory(circle.getCenter(), velocity, contactPointOnEdge)) {
                // If we are here, this is a potential collision

                double distanceToCollision = contactPointOnCircle.distanceTo(contactPointOnEdge);
                double speed = velocity.length();
                double timeToCollision = distanceToCollision / speed;

                Collision collision = new Collision(this, edge, contactPointOnCircle, contactPointOnEdge, timeToCollision);
                collisions.add(collision);
            }

        }

        return collisions;
    }


    private static boolean isPointWithinCollisionTrajectory(Point2D pointOnMovingObject, Vector2D velocity, Point2D point) {
        Vector2D movingObjectToPoint = point.subtract(pointOnMovingObject);
        double dot = Vector2D.dot(movingObjectToPoint, velocity);
        return dot > 0;
    }
}
