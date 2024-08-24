package com.github.berkbavas.breakout.physics.node;

import com.github.berkbavas.breakout.math.*;
import javafx.util.Pair;

import java.util.Optional;

public class ColliderEdge extends LineSegment2D {
    // The line passing through this line segment
    // We store this member for the sake of performant intersection calculations.
    private final Line2D line = Line2D.from(this);

    public ColliderEdge(Point2D P, Point2D Q, String identifier) {
        super(P, Q, identifier);
    }

    public ColliderEdge(Point2D P, Point2D Q) {
        super(P, Q);
    }

    // Finds a pair of points, one is on the line segment and other one is on the circle,
    // which are the closest among all other pairs along the direction.
    // Note that such pair may not exist along given direction vector
    // if the direction and the line segment are parallel or
    // the line segment is not long enough.

    //  Key   = Point on the circle
    //  Value = Point on the line segment

    public Optional<Pair<Point2D, Point2D>> findClosestPairAlongGivenDirection(Circle circle, Vector2D direction) {
        // Case 1:
        // Line passing through the line segment does not intersect the circle.
        //
        //                     ▲
        //                     :
        //                     :   Line
        //                     :
        //                     * Q
        //                     |
        //                     |   Edge
        //        x  x         |
        //     x        x      * P
        //    x  Circle  x     :
        //    x    .     x     :
        //     x        x      :
        //        x  x         ▼
        //


        // Case 2:
        // Line passing through the line segment intersects the circle.
        //

        //             ▲
        //             :
        //             :   Line
        //             :
        //             * Q
        //             |
        //             |   Edge
        //             |
        //             * P
        //             :
        //        x  x :
        //     x       : x
        //    x    .   ▼  x
        //    x  Circle   x
        //     x        x
        //        x  x
        //

        Pair<Point2D, Point2D> result = null;
        Point2D center = circle.getCenter();
        boolean lineIntersectsCircle = circle.doesIntersect(line);

        if (lineIntersectsCircle) {
            Point2D closestVertex = getClosestVertexToPoint(center);
            Point2D pointOnCircle = circle.findIntersectionClosestToRayOrigin(new Ray2D(closestVertex, direction.reversed())).orElse(null);

            if (pointOnCircle != null) {
                result = new Pair<>(pointOnCircle, closestVertex);
            }

        } else {

            Point2D pointOnCircleClosestToLine = circle.findPointOnCircleClosestToLine(line);
            Ray2D rayFromCircleToLine = new Ray2D(pointOnCircleClosestToLine, direction);
            Point2D pointOnLineClosestToCircleAlongDirectionVector = rayFromCircleToLine.findIntersection(line).orElse(null);

            if (pointOnLineClosestToCircleAlongDirectionVector != null) {
                if (isPointOnLineSegment(pointOnLineClosestToCircleAlongDirectionVector)) {
                    result = new Pair<>(pointOnCircleClosestToLine, pointOnLineClosestToCircleAlongDirectionVector);
                } else {
                    Point2D closestVertex = getClosestVertexToPoint(pointOnLineClosestToCircleAlongDirectionVector);
                    Point2D pointOnCircle = circle.findIntersectionClosestToRayOrigin(new Ray2D(closestVertex, direction.reversed())).orElse(null);
                    if (pointOnCircle != null) {
                        result = new Pair<>(pointOnCircle, closestVertex);
                    }
                }
            }
        }

        return Optional.ofNullable(result);
    }
}
