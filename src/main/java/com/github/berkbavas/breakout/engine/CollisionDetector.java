package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.StaticNode;
import com.github.berkbavas.breakout.engine.node.World;
import com.github.berkbavas.breakout.math.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CollisionDetector {

    public Set<Collision> findCollisions(GameObjects gameObjects) {
        Ball ball = gameObjects.getBall();
        World world = gameObjects.getWorld();
        Paddle paddle = gameObjects.getPaddle();

        double speed = ball.getVelocity().length();

        // If the ball is stationary no need to calculate collisions.
        // Return an empty set.
        if (Util.isFuzzyZero(speed)) {
            return Set.of();
        }

        Set<Collision> collisions = new HashSet<>();
        collisions.addAll(findCollisions(world, ball));
        collisions.addAll(findCollisions(paddle, ball));

        return collisions;
    }

    private Set<Collision> findCollisions(StaticNode node, Ball ball) {
        // Ball may collide with a vertex of the node, that is counted as two collisions with two edges of the node.
        // That's why we have set here.
        Set<Collision> collisions = new HashSet<>();

        Set<LineSegment2D> edges = node.getEdges();

        for (LineSegment2D edge : edges) {
            findCollision(edge, ball).ifPresent((Collision collision) -> {
                collision.setCollider(node);
                collisions.add(collision);
            });
        }

        return collisions;
    }

    // Checks if a collision possible between ball and edge given the velocity of ball.
    // This method assumes that edge and ball is not colliding already.
    private Optional<Collision> findCollision(LineSegment2D edge, Ball ball) {
        Vector2D velocity = ball.getVelocity();
        Line2D line = Line2D.from(edge);
        Point2D closestPointToLine = ball.findClosestPointToLine(line);
        Ray2D rayToLine = new Ray2D(closestPointToLine, velocity);

        var ref = new Object() {
            Collision collision = null;
        };

        // Let's check if the ray casting from ball to the line whose origin is the closest point to the line
        // and direction is the velocity intersects the line.
        line.findIntersection(rayToLine).ifPresent((Point2D contact) -> {
            // Okay, the ray intersects the line.
            // But does it intersect the edge as well?

            if (edge.isPointOnLineSegment(contact)) {
                // Contact point is on the edge as well, this means the ray intersects the edge as well.
                ref.collision = createCollision(edge, closestPointToLine, contact, velocity);
            } else {
                // Contact point is not on the line segment.
                // Let's find the closest vertex of the edge to the contact point on the line,
                // and cast a ray originating from this vertex to the ball having direction same as the inverse velocity.
                // If there is an intersection, then this is the collision contact.
                findCollisionBetweenRayFromEdgeToBall(contact, edge, ball).ifPresent((Collision collision) -> ref.collision = collision);
            }

        });

        return Optional.ofNullable(ref.collision);
    }

    //                     ( ) Ball
    //                     /
    //                    /
    //                   /
    //                  /
    //                 /
    //      *         *--------------*
    //   Contact      P     Edge     Q
    //

    private Optional<Collision> findCollisionBetweenRayFromEdgeToBall(Point2D contact, LineSegment2D edge, Ball ball) {
        // Contact point is not on the line segment.
        // Let's find the closest vertex of the edge to the contact point on the line.

        double distance0 = contact.distanceTo(edge.getP());
        double distance1 = contact.distanceTo(edge.getQ());

        Point2D contactPointOnEdge = null;

        if (distance0 < distance1) {
            contactPointOnEdge = edge.getP();
        } else {
            contactPointOnEdge = edge.getQ();
        }

        // Let's cast a ray originating from the closest vertex of edge to the contact point
        // and having direction same as the inverse velocity vector.
        Vector2D inverseVelocity = ball.getVelocity().invert();
        Ray2D rayToBall = new Ray2D(contactPointOnEdge, inverseVelocity);

        // There may be 0, 1 or 2 intersection points.
        List<Point2D> intersections = ball.findIntersection(rayToBall);

        if (intersections.isEmpty()) {
            return Optional.empty();
        }

        // If there are two intersections, choose the closest one to the vertex.
        Point2D contactPointOnBall = null;

        double minDistance = Double.MAX_VALUE;

        for (Point2D intersection : intersections) {
            double distance = intersection.distanceTo(contactPointOnEdge);
            if (distance < minDistance) {
                minDistance = distance;
                contactPointOnBall = intersection;
            }
        }

        assert contactPointOnBall != null;
        return Optional.of(createCollision(edge, contactPointOnBall, contactPointOnEdge, inverseVelocity));
    }

    private Collision createCollision(LineSegment2D edge, Point2D contactPointOnBall, Point2D contactPointOnEdge, Vector2D velocity) {
        double distance = contactPointOnBall.distanceTo(contactPointOnEdge);
        double speed = velocity.length();
        //  Here we assume that the velocity vector
        // and the vector from contact point on edge to contact point on ball
        // has the same direction.
        double timeToCollision = distance / speed;

        Collision collision = new Collision();
        collision.setEdge(edge);
        collision.setContactPointOnBall(contactPointOnBall);
        collision.setContactPointOnEdge(contactPointOnEdge);
        collision.setTimeToCollision(timeToCollision);
        
        return collision;
    }
}
