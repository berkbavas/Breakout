package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.*;

import java.util.*;

public class CollisionDetector {
    private final GameObjects gameObjects;

    public CollisionDetector(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
    }

    public Set<Collision> findPotentialCollisions() {
        Ball ball = gameObjects.getBall();
        World world = gameObjects.getWorld();
        Paddle paddle = gameObjects.getPaddle();
        ArrayList<Brick> bricks = gameObjects.getBricks();

        final double speed = ball.getVelocity().length();

        // If the ball is stationary no need to calculate collisions.
        // Return an empty set.
        if (Util.isFuzzyZero(speed)) {
            return Set.of();
        }

        Set<Collision> potentialCollisions = new HashSet<>();
        potentialCollisions.addAll(findPotentialCollisions(world, ball));
        potentialCollisions.addAll(findPotentialCollisions(paddle, ball));
        potentialCollisions.addAll(findPotentialCollisions(bricks, ball));

        return potentialCollisions;
    }

    private static Set<Collision> findPotentialCollisions(List<Brick> bricks, Ball ball) {
        Set<Collision> collisions = new HashSet<>();

        for (Brick brick : bricks) {
            if (brick.isHit()) {
                continue;
            }
            collisions.addAll(findPotentialCollisions(brick, ball));
        }

        return collisions;
    }

    private static Set<Collision> findPotentialCollisions(StaticNode node, Ball ball) {
        // Ball may collide with a vertex of the node, that is counted as two collisions with two edges of the node.
        // That's why we have set here.
        Set<Collision> collisions = new HashSet<>();
        Set<LineSegment2D> edges = node.getEdges();
        for (LineSegment2D edge : edges) {
            findPotentialCollision(node, edge, ball).ifPresent(collisions::add);
        }

        return collisions;
    }

    // Checks if a collision is potential between the ball and edge.
    // This method assumes that edge and ball is not colliding already.

    // Case 1:
    // Line passing through the edge does not intersect the circle.
    //
    //                     ▲
    //                     |   Line
    //                     |
    //                     * Q
    //                     |
    //                     |   Edge = [P, Q]
    //        *  *         |
    //     *        *      * P
    //    *  Circle  *     |
    //    *    .     *     |
    //     *        *      ▼
    //        *  *
    //


    // Case 2:
    // Line passing through the edge does intersect the circle.
    //
    //             ▲
    //             |   Line
    //             |
    //             * Q
    //             |
    //             |   Edge = [P, Q]
    //             |
    //             * P
    //        *  * |
    //     *       |*
    //    *    .   ▼ *
    //    *  Circle   *
    //     *        *
    //        *  *
    //

    private static Optional<Collision> findPotentialCollision(StaticNode node, LineSegment2D edge, Ball ball) {
        Line2D line = Line2D.from(edge);

        if (ball.findIntersection(line).isEmpty()) {
            return findPotentialCollisionCase1(node, edge, ball);
        } else {
            return findPotentialCollisionCase2(node, edge, ball);
        }
    }

    private static Optional<Collision> findPotentialCollisionCase1(StaticNode node, LineSegment2D edge, Ball ball) {
        Line2D line = Line2D.from(edge);

        Point2D pointOnCircleClosestToLine = ball.findPointOnCircleClosestToLine(line);

        Vector2D velocity = ball.getVelocity();
        Point2D centerOfBall = ball.getCenter();

        if (!isPointInsideCriticalRegion(centerOfBall, pointOnCircleClosestToLine, velocity)) {
            return Optional.empty();
        }

        // If we are here, then the point is inside the critical region, i.e,
        // it might lie in the collision trajectory of the ball.

        // Next we cast a ray originating from the point on circle closest to the line along the direction of velocity
        // in order to find collision contact point on the line.

        Optional<Point2D> maybe = line.findIntersection(new Ray2D(pointOnCircleClosestToLine, velocity));

        if (maybe.isEmpty()) {
            return Optional.empty();
        }

        Point2D contactPointOnLine = maybe.get();

        // Check if this point is on the edge as well.
        // If it is on the edge, then this is the collision contact point on edge as
        // it lies inside the collision trajectory region of the ball
        boolean isContactPointOnLineOnEdgeAsWell = edge.isPointOnLineSegment(contactPointOnLine);

        if (isContactPointOnLineOnEdgeAsWell) {
            // We found a potential collision with edge.
            // Let's calculate the time of impact for given velocity and collision contacts (our points above).
            // Here the point on the circle closest to the line is the contact point on the ball.
            Collision collision = constructCollision(node, edge, pointOnCircleClosestToLine, contactPointOnLine, velocity);
            return Optional.of(collision);

        } else {
            // If the point is not on the edge, then we follow another logic.

            // First we find the vertex of the edge that is closest the contact point on the line.
            Point2D closestVertexToContactPointOnLine = edge.getClosestVertexToPoint(contactPointOnLine);

            // Cast a ray originating from this point along the direction of the inverse velocity vector.
            Ray2D rayOriginationFromClosestVertex = new Ray2D(closestVertexToContactPointOnLine, velocity.reversed());
            Optional<Point2D> contactPointOnBall = ball.findIntersectionClosestToOriginOfRay(rayOriginationFromClosestVertex);
            // Here contactPointOnBall might be empty.
            // If there is no intersection then it means that the edge does not lie in the collision trajectory of the ball.

            if (contactPointOnBall.isPresent()) {
                // We found a potential collision with the edge.

                // Here the vertex of the edge closest to the 'speculative' contact point on the line
                // is the 'actual' contact point.
                Collision collision = constructCollision(node, edge, contactPointOnBall.get(), closestVertexToContactPointOnLine, velocity);
                return Optional.of(collision);
            }
        }

        // If we are here, then there is no potential collision with the edge along the direction of the velocity.
        return Optional.empty();
    }

    private static Optional<Collision> findPotentialCollisionCase2(StaticNode node, LineSegment2D edge, Ball ball) {
        // First find the vertex closest to the circle.
        Point2D center = ball.getCenter();

        Point2D closestVertex;
        double distance0 = edge.getP().distanceTo(center);
        double distance1 = edge.getQ().distanceTo(center);

        if (distance0 < distance1) {
            closestVertex = edge.getP();
        } else {
            closestVertex = edge.getQ();
        }

        // Cast a ray originating from the closest vertex to the center of circle.
        Ray2D rayFromClosestVertexToCenterOfCircle = new Ray2D(closestVertex, center.subtract(closestVertex));

        // Choose the closest intersection point on the circle to the edge.
        Optional<Point2D> maybe = ball.findIntersectionClosestToOriginOfRay(rayFromClosestVertexToCenterOfCircle);

        // rayFromClosestVertexToCenterOfCircle and circle must intersect by the construction of rayFromClosestVertexToCenterOfCircle.
        // In order to be on the safe side, return here.
        if (maybe.isEmpty()) {
            return Optional.empty();
        }

        Point2D pointOnCircleClosestToEdge = maybe.get();

        // Now cast a ray originating from the point on circle that is closest to the edge and along the direction of
        // the velocity vector.
        Ray2D rayFromPointOnCircleAlongVelocityDirection = new Ray2D(pointOnCircleClosestToEdge, ball.getVelocity());

        var ref = new Object() {
            Point2D contactPointOnEdge = null;
            Point2D contactPointOnCircle = null;
        };

        rayFromPointOnCircleAlongVelocityDirection.findIntersection(edge)
                .ifPresent((Point2D point) -> ref.contactPointOnEdge = point);

        if (ref.contactPointOnEdge != null) {
            Collision collision = constructCollision(node, edge, pointOnCircleClosestToEdge, ref.contactPointOnEdge, ball.getVelocity());
            return Optional.of(collision);
        }

        // Now cast a ray from the closest vertex of the edge along the direction of reverse velocity vector
        // in order to find the contact point on the circle for a potential collision.
        Ray2D rayFromClosestVertexOfEdgeHavingDirectionReverseVelocityVector = new Ray2D(closestVertex, ball.getVelocity().reversed());

        ball.findIntersectionClosestToOriginOfRay(rayFromClosestVertexOfEdgeHavingDirectionReverseVelocityVector)
                .ifPresent((Point2D point) -> ref.contactPointOnCircle = point);

        if (ref.contactPointOnCircle != null) {
            Collision collision = constructCollision(node, edge, ref.contactPointOnCircle, closestVertex, ball.getVelocity().reversed());
            return Optional.of(collision);
        }

        return Optional.empty();
    }


    //
    //                          |
    //                          |
    //                          |
    //                 Velocity |
    //                    <---  *  Center of the ball
    //      *                   |
    //    Point                 |             *
    //                          |            Point
    //                          |
    //    Critical Region

    private static boolean isPointInsideCriticalRegion(Point2D center, Point2D point, Vector2D velocity) {
        // If the vector origination from center to point has positive dot product with the velocity vector,
        // then it is inside the critical region.
        Vector2D centerToPoint = point.subtract(center);
        double dot = Vector2D.dot(centerToPoint, velocity);
        return dot > 0;
    }

    private static Collision constructCollision(StaticNode node, LineSegment2D edge, Point2D contactPointOnBall, Point2D contactPointOnEdge, Vector2D velocity) {
        double distanceToCollision = contactPointOnBall.distanceTo(contactPointOnEdge);
        double speed = velocity.length();
        double timeToCollision = distanceToCollision / speed;

        Collision collision = new Collision();
        collision.setCollider(node);
        collision.setEdge(edge);
        collision.setContactPointOnBall(contactPointOnBall);
        collision.setContactPointOnEdge(contactPointOnEdge);
        collision.setTimeToCollision(timeToCollision);

        return collision;
    }

    // Cast a ray originating from a point on the circle along the given direction.
    // This method assumes that the ray intersects the line.
    public static Optional<Point2D> findIntersectionUnsafe(Line2D line, Point2D pointOnCircle, Vector2D direction) {
        return line.findIntersection(new Ray2D(pointOnCircle, direction));
    }

    // Cast a ray originating from point and pointing the center of the circle.
    // This ray must intersect the circle inevitably.
    public static Point2D findIntersectionUnsafe(Circle circle, Point2D point) {
        Point2D center = circle.getCenter();
        Vector2D direction = center.subtract(point);

        List<Point2D> intersections = circle.findIntersection(new Ray2D(center, direction));
        assert intersections.size() == 2;

        Point2D i0 = intersections.get(0);
        Point2D i1 = intersections.get(1);

        // Find the closest one to the point.
        double distance0 = point.distanceTo(i0);
        double distance1 = point.distanceTo(i1);

        return distance0 < distance1 ? i0 : i1;
    }
}
