package com.github.berkbavas.breakout.physics.simulator.helper;

import com.github.berkbavas.breakout.math.*;
import com.github.berkbavas.breakout.physics.node.base.ColliderEdge;
import com.github.berkbavas.breakout.util.ReturnValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CriticalPointFinder {

    // There are 3 types of critical points between an edge and circle along the given direction vector.
    //
    // SEPARATE:
    // Separate critical points are two distinct points, one is on the edge and the other one is on the circle,
    // which are closest to each other among all their siblings.
    //
    //                     * Q
    //                     │
    //                     │
    //                     │ Edge
    //        x  x         │
    //     x        x      │
    //    x  Circle  x     * P
    //    x    .     x
    //     x        x
    //        x  x
    //
    //
    // TANGENTIAL:
    // If the edge is tangent to the circle, then this tangency is called tangential critical point.
    //
    //                 * Q
    //        x  x     │
    //     x        x  │
    //    x  Circle  x │  Edge
    //    x    .     x │
    //     x        x  │
    //        x  x     * P
    //
    //
    // CUTTING:
    // If the edge is secant to the circle, then the secant points are called cutting critical points.
    // Note that there may be two or one cutting critical points depending on the edge.
    //
    //            * Q
    //            │
    //        x  x│
    //     x      │ x
    //    x       │  x
    //    x       │  x
    //     x      │ x
    //        x  x│
    //            │
    //            * P


    public static Optional<CriticalPointPair> findCriticalPointsAlongGivenDirection(Circle circle, ColliderEdge edge, Vector2D direction) {
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

        ReturnValue<CriticalPointPair> ref = new ReturnValue<>(null);
        Line2D line = edge.getLine();
        List<Point2D> intersections = circle.findIntersection(line);

        if (intersections.isEmpty()) {
            // Case 1
            if (direction.l2norm() != 0) {
                Point2D pointOnCircleClosestToLine = circle.findPointOnCircleClosestToLine(line);
                Ray2D rayFromCircleToLine = new Ray2D(pointOnCircleClosestToLine, direction);
                rayFromCircleToLine.findIntersection(line).ifPresent(pointOnLine -> {
                    if (edge.isPointOnLineSegment(pointOnLine)) {
                        ref.value = new SeparateCriticalPointPair(pointOnCircleClosestToLine, pointOnLine);
                    } else {
                        Point2D closestVertex = edge.getClosestVertexToPoint(pointOnLine);
                        Ray2D rayFromVertexToCircle = new Ray2D(closestVertex, direction.reversed());
                        circle.findIntersectionClosestToRayOrigin(rayFromVertexToCircle)
                                .ifPresent(pointOnCircle -> ref.value = new SeparateCriticalPointPair(pointOnCircle, closestVertex));
                    }
                });
            }
        } else {
            // Case 2

            // Check that the line segment intersect the circle.
            List<Point2D> pointsOnLineSegment = new ArrayList<>();

            for (Point2D intersection : intersections) {
                if (edge.isPointOnLineSegment(intersection)) {
                    pointsOnLineSegment.add(intersection);
                }
            }

            if (pointsOnLineSegment.size() == 1) {
                Point2D point = pointsOnLineSegment.get(0);
                ref.value = new TangentialCriticalPoint(point);
            } else if (pointsOnLineSegment.size() == 2) {
                ref.value = new CuttingCriticalPointPair(pointsOnLineSegment);
            }

            if (ref.value == null) {
                // If we are here, then the line segment does not intersect the circle at all.
                if (direction.l2norm() != 0) {
                    Point2D center = circle.getCenter();
                    Point2D closestVertex = edge.getClosestVertexToPoint(center);
                    Ray2D rayFromLineSegmentToCircle = new Ray2D(closestVertex, direction.reversed());
                    circle.findIntersectionClosestToRayOrigin(rayFromLineSegmentToCircle)
                            .ifPresent(pointOnCircle -> ref.value = new SeparateCriticalPointPair(pointOnCircle, closestVertex));
                }
            }
        }

        return Optional.ofNullable(ref.value);
    }

    public static Optional<CriticalPointPair> findConflictingCriticalPoints(Circle circle, ColliderEdge edge) {
        ReturnValue<CriticalPointPair> ref = new ReturnValue<>(null);

        Line2D line = edge.getLine();
        List<Point2D> intersections = circle.findIntersection(line);

        List<Point2D> pointsOnLineSegment = new ArrayList<>();

        for (Point2D intersection : intersections) {
            if (edge.isPointOnLineSegment(intersection)) {
                pointsOnLineSegment.add(intersection);
            }
        }

        if (pointsOnLineSegment.size() == 1) {
            Point2D point = pointsOnLineSegment.get(0);
            ref.value = new TangentialCriticalPoint(point);
        } else if (pointsOnLineSegment.size() == 2) {
            ref.value = new CuttingCriticalPointPair(pointsOnLineSegment);
        }

        return Optional.ofNullable(ref.value);
    }

}
