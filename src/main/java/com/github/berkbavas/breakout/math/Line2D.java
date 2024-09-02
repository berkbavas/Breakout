package com.github.berkbavas.breakout.math;

import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@ToString
@Getter
public class Line2D {
    private final Point2D P;
    private final Point2D Q;

    private final double slope;

    private final double A;
    private final double B;
    private final double C;

    private final Vector2D direction;

    // A line in 2D Cartesian plane passing through two points P = (x0, y0) and Q = (x1, y1)
    // is defined by the equation
    // Ax + By + C = 0 where
    // A = y1 - y0,                              A = Qy - Py
    // B = x0 - x1,                              B = Px - Qx
    // C = -A * x0 - B * y0                      C = -A*Px - B*Py

    // Slope is (y1 - y0) / (x1 - x0) if x0 != x1 and undefined if x0 == x1.

    public Line2D(Point2D P, Point2D Q) {
        this.P = P;
        this.Q = Q;

        Double[] coefficients = calculateEquationCoefficients(P, Q);
        this.A = coefficients[0];
        this.B = coefficients[1];
        this.C = coefficients[2];

        this.slope = calculateSlope(P, Q);
        this.direction = Q.subtract(P).normalized();
    }

    public static Double[] calculateEquationCoefficients(Point2D P, Point2D Q) {
        // A = Qy - Py
        // B = Px - Qx
        // C = -A*Px - B*Py

        double Px = P.getX();
        double Py = P.getY();
        double Qx = Q.getX();
        double Qy = Q.getY();

        double A = Qy - Py;
        double B = Px - Qx;
        double C = -A * Px - B * Py;

        return new Double[]{A, B, C};
    }

    public static double calculateSlope(Point2D p0, Point2D p1) {
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


        if (Util.fuzzyCompare(p0.getX(), p1.getX())) {
            return Double.NaN;
        } else {
            return (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        }
    }

    public boolean isPointOnLine(Point2D point) {
        // Check that point = (x,y) satisfies the equation Ax + By + C = 0.
        return Util.isFuzzyZero(A * point.getX() + B * point.getY() + C);
    }

    public boolean isParallelTo(Line2D other) {
        return Util.fuzzyCompare(slope, other.slope);
    }

    public Optional<Point2D> findIntersection(Line2D other) {
        // If two lines intersect at point (x,y) then we must have
        //
        // A0*x + B0*y + C0 = 0
        // A1*x + B1*y + C1 = 0
        //
        // If these two lines are not parallel, then the solution for the equations above is the intersection point
        // of these two lines because such intersection is unique.

        //
        // [A0, B0; A1, B1] * [x; y] + [C0; C1] = [0; 0]
        // or
        // [A0, B0; A1, B1] * [x; y] = [-C0; -C1]
        //

        return Matrix2x2.solve(this, other);
    }


    // Reference: https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
    public double calculateDistanceToPoint(Point2D point) {
        final double x0 = point.getX();
        final double y0 = point.getY();

        final double x1 = P.getX();
        final double y1 = P.getY();

        final double x2 = Q.getX();
        final double y2 = Q.getY();

        final double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        final double denominator = Q.distanceTo(P);

        return numerator / denominator;
    }

    //
    //             x  x
    //          x        x
    //         x    .     x
    //         x  Circle  x
    //          x        x
    //             x  x
    //     P                  Q
    //     *------------------*---►
    //   Origin            Direction
    //

    public Point2D findClosestPointToCircleCenter(Circle circle) {
        Point2D center = circle.getCenter();
        Vector2D originToCenter = center.subtract(P);
        double dot = direction.dot(originToCenter);
        return P.add(direction.multiply(dot));
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

    public static Line2D from(Ray2D ray) {
        return new Line2D(ray.getOrigin(), ray.calculate(1.00));
    }

    public static Line2D from(LineSegment2D ls) {
        return new Line2D(ls.getP(), ls.getQ());
    }
}

