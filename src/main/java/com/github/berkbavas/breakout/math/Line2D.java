package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Line2D {
    private final Point2D p;
    private final Point2D q;
    private final double slope;

    public Line2D(Point2D p, Point2D q) {
        this.p = p;
        this.q = q;
        this.slope = calculateSlope(p, q);
    }

    //    There are 3 cases for the slope calculation.

    //    Case 1:
    //    Slope is (y2-y1) / (x2-x1).
    //
    //     \
    //      *  q = (x2, y2)
    //       \
    //        \
    //         \
    //          \
    //           *  p = (x1, y1)
    //            \


    //    Case 2:
    //    Slope is 0.
    //
    //    <----*---------------------------*------>
    //         p = (x1, c)                 q = (x2, c)


    //    Case 3:
    //    Slope is undefined
    //
    //    |
    //    *  q = (c, y2)
    //    |
    //    |
    //    |
    //    |
    //    *  p = (c, y1)
    //    |

    public static double calculateSlope(Point2D p0, Point2D p1) {
        if (Util.fuzzyCompare(p0.getX(), p1.getX())) {
            return Double.NaN;
        } else {
            return (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        }
    }

    public boolean isPointOnLine(Point2D point) {
        // If the slope of a line passing through point and p
        // is the same as this line's slope then the given point is
        // on this line.

        final double slope = calculateSlope(point, p);

        if (Double.isNaN(slope)) {
            if (point.equals(p)) {
                return true;
            }
        }

        return Util.fuzzyCompare(this.slope, slope);
    }

    public Optional<Point2D> findIntersection(Line2D other) {
        // First check if they are parallel.
        final boolean parallel = Util.fuzzyCompare(slope, other.slope);

        if (parallel) {
            if (isPointOnLine(other.getP())) {
                return Optional.of(p);
            } else {
                return Optional.empty();
            }
        }

        //
        //         \
        //          * other.q
        //           \
        //            \  \ dir1
        //             \
        //    <----*---------------------------*------>
        //         p     \     --->            q
        //     (origin0)  \       dir0
        //                 \
        //                  * other.p
        //                   \    (origin1)
        //

        final Point2D origin0 = p;
        final Point2D origin1 = other.p;

        final Vector2D dir0 = new Vector2D(q.x - p.x, q.y - p.y);
        final Vector2D dir1 = new Vector2D(other.q.x - other.p.x, other.q.y - other.p.y);

        final Matrix2x1 lhs = new Matrix2x1(origin1.x - origin0.x, origin1.y - origin0.y);
        final Matrix2x2 rhs = new Matrix2x2(dir0.x, -dir1.x, dir0.y, -dir1.y);

        final Optional<Matrix2x1> solution = Matrix2x2.solve(lhs, rhs);

        if (solution.isPresent()) {
            final double t = solution.get().getM00();
            return Optional.of(origin0.add(dir0.multiply(t)));
        }

        return Optional.empty();
    }

    public Optional<Point2D> findIntersection(Ray2D ray) {
        return ray.findIntersection(this);
    }

    // Reference: https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
    public double calculateDistance(Point2D point) {
        final double x0 = point.x;
        final double y0 = point.y;

        final double x1 = p.x;
        final double y1 = p.y;

        final double x2 = q.x;
        final double y2 = q.y;

        final double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        final double denominator = q.distanceTo(p);

        return numerator / denominator;
    }

    public Point2D findClosestPointToCircleCenter(Circle circle) {
        Ray2D ray = Ray2D.from(this);
        Point2D origin = ray.getOrigin();
        Vector2D direction = ray.getDirection();
        Vector2D originToCenter = circle.getCenter().subtract(origin);
        double dot = direction.dot(originToCenter);
        return origin.add(direction.multiply(dot));
    }

    public static Line2D from(LineSegment2D ls) {
        return new Line2D(ls.getP(), ls.getQ());
    }

    public static Line2D from(Ray2D ray) {
        return new Line2D(ray.getOrigin(), ray.calculatePointAt(1.0));
    }

    @Override
    public String toString() {
        return String.format("Line2D{p = %s, q = %s, slope = %.4f}", p, q, slope);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Line2D) {
            Line2D other = (Line2D) object;
            return Util.fuzzyCompare(slope, other.slope) && isPointOnLine(other.getP());
        } else {
            return false;
        }
    }
}
