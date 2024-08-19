package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.*;
import javafx.util.Pair;

import java.util.*;

public final class CollisionDetector {

    private CollisionDetector() {
    }

    public static Set<Collision> findPotentialCollisions(GameObjects gameObjects) {
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
        List<LineSegment2D> edges = node.getEdges();
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
        Line2D linePassingThroughEdge = Line2D.from(edge);
        Point2D pointOnBallClosestToLine = ball.findPointOnCircleClosestToLine(linePassingThroughEdge);

        if (!isPointInCriticalRegion(ball, pointOnBallClosestToLine)) {
            return Optional.empty();
        }

        Vector2D velocity = ball.getVelocity();

        // If we are here, then the point is inside the critical region, i.e,
        // it might lie in the collision trajectory of the ball.

        // Next we cast a ray originating from the point on circle closest to the line along the direction of velocity
        // in order to find collision contact point on the line.

        Ray2D rayFromBallToLine = new Ray2D(pointOnBallClosestToLine, velocity);
        Point2D contactPointOnLine = linePassingThroughEdge.findIntersection(rayFromBallToLine).orElse(null);

        if (contactPointOnLine == null) {
            return Optional.empty();
        }

        // Check if this point is on the edge as well.
        // If it is on the edge, then this is the collision contact point on edge as
        // it lies inside the collision trajectory region of the ball
        boolean isContactPointOnLineOnEdgeAsWell = edge.isPointOnLineSegment(contactPointOnLine);

        if (isContactPointOnLineOnEdgeAsWell) {
            // We found a potential collision with edge.
            // Let's calculate the time of impact for given velocity and collision contacts (our points above).
            // Here the point on the circle closest to the line is the contact point on the ball.
            Collision collision = constructCollision(node, edge, pointOnBallClosestToLine, contactPointOnLine, velocity);
            return Optional.of(collision);

        } else {
            // If the point is not on the edge, then we follow another logic.

            // First we find the vertex of the edge that is closest the contact point on the line.
            Point2D closestVertexToContactPointOnLine = edge.getClosestVertexToPoint(contactPointOnLine);

            // Cast a ray originating from this point along the direction of the inverse velocity vector.
            Ray2D rayFromEdgeToBall = new Ray2D(closestVertexToContactPointOnLine, velocity.reversed());
            Point2D contactPointOnBall = ball.findIntersectionClosestToOriginOfRay(rayFromEdgeToBall).orElse(null);
            // Here if rayFromEdgeToBall and ball do not intersect, then contactPointOnBall is null.
            // In other words, if there is no intersection between the ray and ball,
            // then it means that the edge does not lie in the collision trajectory of the ball.

            if (contactPointOnBall != null) {
                // We found a potential collision with the edge.

                Collision collision = constructCollision(node, edge, contactPointOnBall, closestVertexToContactPointOnLine, velocity);
                return Optional.of(collision);
            }
        }

        // If we are here, then there is no potential collision with the edge along the direction of the velocity.
        return Optional.empty();
    }

    private static Optional<Collision> findPotentialCollisionCase2(StaticNode node, LineSegment2D edge, Ball ball) {
        Vector2D velocity = ball.getVelocity();

        // Find the point on edge closest to the circle and its counterpart on the circle.
        var pair = ball.findPointOnEdgeClosestToCircle(edge);
        Point2D pointOnEdgeClosestToBall = pair.getKey();
        Point2D pointOnBallClosestToEdge = pair.getValue();

        if (!isPointInCriticalRegion(ball, pointOnEdgeClosestToBall)) {
            return Optional.empty();
        }

        // Now cast a ray originating from the point on circle that is closest to the edge
        // along the direction of the velocity vector.
        Ray2D rayFromPointOnBallAlongDirectionOfVelocity = new Ray2D(pointOnBallClosestToEdge, velocity);
        Point2D contactPointOnEdge = rayFromPointOnBallAlongDirectionOfVelocity.findIntersection(edge).orElse(null);

        if (contactPointOnEdge != null) {
            Collision collision = constructCollision(node, edge, pointOnBallClosestToEdge, contactPointOnEdge, velocity);
            return Optional.of(collision);
        }

        // Now cast a ray from the closest vertex of the edge along the direction of reverse velocity vector
        // in order to find the contact point on the circle for a potential collision.
        Ray2D rayFromClosestVertexOfEdgeAlongInverseVelocityDirection = new Ray2D(pointOnEdgeClosestToBall, velocity.reversed());
        Point2D contactPointOnCircle = ball.findIntersectionClosestToOriginOfRay(rayFromClosestVertexOfEdgeAlongInverseVelocityDirection).orElse(null);

        if (contactPointOnCircle != null) {
            Collision collision = constructCollision(node, edge, contactPointOnCircle, pointOnEdgeClosestToBall, velocity.reversed());
            return Optional.of(collision);
        }

        return Optional.empty();
    }


    // Finds a potential collision between moving edge and steady circle.
    // This method is used when updating the position of paddle in such that the paddle does not collide with the circle
    // as it moves to its new position.
    // Returns a pair of points where the 'key' is the contact point on edge and the 'value' is the contact point on the circle
    // if a collision is potential along the given direction between moving edge and steady circle.
    public static Optional<Pair<Point2D, Point2D>> findCollision(LineSegment2D edge, Vector2D velocity, Circle circle) {
        Pair<Point2D, Point2D> pairs = circle.findPointOnEdgeClosestToCircle(edge);
        Point2D pointOnEdgeClosestToCircle = pairs.getKey();
        Point2D pointOnCircleClosestToEdge = pairs.getValue();

        // Is circle on the collision trajectory?
        if (!isPointInCollisionTrajectory(circle.getCenter(), pointOnCircleClosestToEdge, velocity)) {
            // No potential collision will occur.
            return Optional.empty();
        }

        Ray2D rayFromEdgeToCircle = new Ray2D(pointOnEdgeClosestToCircle, velocity);
        List<Point2D> intersections = circle.findIntersection(rayFromEdgeToCircle);
        Point2D contactPointOnCircle = Point2D.findClosestPoint(pointOnEdgeClosestToCircle, intersections);

        if (contactPointOnCircle != null) {
            return Optional.of(new Pair<>(pointOnEdgeClosestToCircle, contactPointOnCircle));
        }

        // Cast a ray originating from pointOnCircleClosestToEdge along the 'direction' vector.
        Ray2D rayFromCircle = new Ray2D(pointOnCircleClosestToEdge, velocity);
        Point2D contactPointOnEdge = edge.findIntersection(rayFromCircle).orElse(null);

        if (contactPointOnEdge != null) {
            return Optional.of(new Pair<>(contactPointOnEdge, pointOnCircleClosestToEdge));
        }

        return Optional.empty();
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

    //
    //                                |
    //                                |
    //                                |
    //                       Velocity |
    //                          <---  *  Ball
    //         *                      |
    //       Point (Critical)         |             *
    //                                |           Point (Not critical)
    //                                |
    //        Critical Region

    private static boolean isPointInCriticalRegion(Ball ball, Point2D point) {
        Vector2D ballToPoint = point.subtract(ball.getCenter());
        double dot = Vector2D.dot(ballToPoint, ball.getVelocity());
        return dot > 0;
    }

    private static boolean isPointInCollisionTrajectory(Point2D steadyPoint, Point2D pointOnMovingObject, Vector2D velocity) {
        Vector2D toSteadyPoint = steadyPoint.subtract(pointOnMovingObject);
        double dot = Vector2D.dot(toSteadyPoint, velocity);
        return dot > 0;
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
