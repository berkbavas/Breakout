package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Ray2D {
    private final Point2D origin;
    private final Vector2D direction;

    // These are the coefficients of the equation of the line passing through this ray.
    // We cache these for the sake of fast intersection calculations.
    private final double A;
    private final double B;
    private final double C;

    public Ray2D(Point2D origin, Vector2D direction) {
        this.origin = origin;
        this.direction = direction.normalized();

        Point2D P = origin;
        Point2D Q = origin.add(direction.multiply(1));

        Double[] coefficients = Line2D.calculateEquationCoefficients(P, Q);
        this.A = coefficients[0];
        this.B = coefficients[1];
        this.C = coefficients[2];
    }

    public Point2D calculate(double t) {
        return origin.add(direction.multiply(t));
    }

    public Optional<Double> findParameterForGivenPoint(Point2D point) {

        boolean isPointOnBidirectionalRay = isPointOnBidirectionalRay(point);

        if (!isPointOnBidirectionalRay) {
            return Optional.empty();
        }

        // If we are here, we are sure that the point can be written as
        // Point = Origin + t * Direction, where t is a real number.

        // t = (Px - Ox) / Dx
        // or
        // t = (Py - Oy) / Dy
        // (Dx, Dy) cannot be (0,0) by the definition of ray.

        final double Ox = origin.getX();
        final double Oy = origin.getY();

        final double Px = point.getX();
        final double Py = point.getY();

        final double Dx = direction.getX();
        final double Dy = direction.getY();

        final double t = (Px - Ox) / Dx;
        final double s = (Py - Oy) / Dy;

        if (Util.isFuzzyZero(Dx)) {
            return Optional.of(s);
        } else if (Util.isFuzzyZero(Dy)) {
            return Optional.of(t);
        } else if (Util.fuzzyCompare(t, s)) {
            return Optional.of(t);
        }

        return Optional.empty();
    }

    public boolean isParallelTo(Ray2D ray) {
        boolean same = direction.equals(ray.direction);
        boolean opposite = direction.equals(ray.direction.reversed());
        return same || opposite;
    }

    public boolean isCollinear(Ray2D other) {
        boolean isOtherOriginOnThisRay = isPointOnBidirectionalRay(other.origin);
        boolean isParallel = isParallelTo(other);
        return isParallel && isOtherOriginOnThisRay;
    }

    public boolean isPointOnBidirectionalRay(Point2D point) {
        // Bidirectional ray is the same as the line passing through this ray.
        // Hence, check that point satisfy the line equation.
        return Util.isFuzzyZero(A * point.getX() + B * point.getY() + C);
    }

    public boolean isPointOnRay(Point2D point) {
        Double t = findParameterForGivenPoint(point).orElse(null);

        if (t != null) {
            return 0 <= t;
        }

        return false;
    }

    public Optional<Point2D> findIntersection(Line2D line) {
        return Matrix2x2.solve(A, B, C, line.getA(), line.getB(), line.getC()).map((intersection) -> {
            if (isPointOnRay(intersection)) {
                return intersection;
            }
            return null;
        });
    }

    public Optional<Point2D> findIntersection(Ray2D other) {
        return Matrix2x2.solve(A, B, C, other.A, other.B, other.C).map((intersection) -> {
            if (isPointOnRay(intersection) && other.isPointOnRay(intersection)) {
                return intersection;
            }
            return null;
        });
    }

    public Optional<Point2D> findIntersection(LineSegment2D ls) {
        return Matrix2x2.solve(A, B, C, ls.getA(), ls.getB(), ls.getC()).map((intersection) -> {
            if (isPointOnRay(intersection) && ls.isPointOnLineSegment(intersection)) {
                return intersection;
            }
            return null;
        });
    }

    public Point2D findClosestPointToCenterOfCircle(Circle circle) {
        //
        //             x  x
        //          x        x
        //         x    .     x
        //         x  Circle  x
        //          x        x
        //             x  x
        //
        //              ↙ Closest Point to the Center of the Circle
        //    (*)----------------►
        //   Origin         Direction
        //

        Vector2D originToCenter = circle.getCenter().subtract(origin);

        double dot = direction.dot(originToCenter);

        if (dot <= 0.0) {
            // If dot is negative then the closest point is the origin.
            // In fact, if dot product is negative, then the point on the line passing through this ray that is
            // closest to circle lies on other direction of this ray.

            //             x  x
            //          x        x          Closest Point to the Center of the Circle
            //         x    .     x           ↙
            //         x  Circle  x         (*)----------------►
            //          x        x         Origin         Direction
            //             x  x
            //

            dot = 0.0;
        }

        return origin.add(direction.multiply(dot));
    }

    @Override
    public String toString() {
        return String.format("Ray2D{origin = %s, direction = %s}", origin, direction);
    }

}
