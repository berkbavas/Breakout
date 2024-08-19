package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class Ray2D {
    private final Point2D origin;
    private final Vector2D direction;

    public Ray2D(Point2D origin, Vector2D direction) {
        this.origin = origin;
        this.direction = direction.normalized();
    }

    public Point2D calculatePointAt(double t) {
        final double x = origin.x + t * direction.x;
        final double y = origin.y + t * direction.y;
        return new Point2D(x, y);
    }

    public Optional<Double> findParameterForGivenPoint(Point2D point) {

        // P: Point
        // O: Origin of the ray
        // D: Direction of the ray
        //
        // Px = Ox + t * Dx  => t = (Px - Ox) / Dx when Dx != 0
        // Py = Oy + t * Dy
        // Therefore we must have Py = Oy + ((Px - Ox) / Dx) * Dy

        // There are 4 cases:
        // Both Dx and Dy may be zero
        // Dx may be zero and Dy may be nonzero
        // Dy may be zero and Dx may be nonzero
        // Both Dx and Dy may be nonzero

        Optional<Double> result = Optional.empty();

        if (Util.isFuzzyZero(direction.x) && Util.isFuzzyZero(direction.y)) { // Both Dx and Dy are zero
            if (Util.fuzzyCompare(point.x, origin.x) && Util.fuzzyCompare(point.y, origin.y)) {
                result = Optional.of(0.0);
            }
        } else if (Util.isFuzzyZero(direction.x)) { // Dx is zero and Dy is nonzero
            if (Util.fuzzyCompare(point.x, origin.x)) {
                final double t = (point.y - origin.y) / direction.y;
                result = Optional.of(t);
            }
        } else if (Util.isFuzzyZero(direction.y)) { // Dy is zero and Dx is nonzero
            if (Util.fuzzyCompare(point.y, origin.y)) {
                final double t = (point.x - origin.x) / direction.x;
                result = Optional.of(t);
            }
        } else { // Both Dx and Dy are nonzero
            final double t = (point.x - origin.x) / direction.x; //  t = (Px - Ox) / Dx
            final double s = (point.y - origin.y) / direction.y; //  s = (Py - Oy) / Dy
            if (Util.fuzzyCompare(t, s)) {  // t and s must be equal if such parameter exist
                result = Optional.of(t);
            }
        }

        return result;
    }

    public boolean isCollinear(Ray2D other) {
        Optional<Double> parameter = findParameterForGivenPoint(other.origin);

        if (parameter.isEmpty()) {
            return false;
        }

        boolean same = direction.equals(other.direction);
        boolean opposite = direction.equals(other.direction.reversed());

        return same || opposite;
    }

    public boolean isPointOnRay(Point2D point) {
        var ref = new Object() {
            boolean result = false;
        };

        findParameterForGivenPoint(point).ifPresent((Double parameter) -> {
            ref.result = Util.isGreaterThanOrEqualToZero(parameter);
        });

        return ref.result;
    }

    public Optional<Double> findParameterIfIntersects(Ray2D other) {
        final Point2D origin0 = origin;
        final Point2D origin1 = other.origin;

        final Vector2D dir0 = direction;
        final Vector2D dir1 = other.direction;

        final Matrix2x1 lhs = new Matrix2x1(origin1.x - origin0.x, origin1.y - origin0.y);
        final Matrix2x2 rhs = new Matrix2x2(dir0.x, -dir1.x, dir0.y, -dir1.y);

        final Optional<Matrix2x1> solution = Matrix2x2.solve(lhs, rhs);

        if (solution.isPresent()) {
            final double t = solution.get().getM00();
            final double s = solution.get().getM10();

            if (Util.isGreaterThanOrEqualToZero(t) && Util.isGreaterThanOrEqualToZero(s)) {
                return Optional.of(t);
            }
        }

        return Optional.empty();
    }

    public Optional<Point2D> findIntersection(Ray2D other) {
        var ref = new Object() {
            Point2D intersection = null;
        };

        findParameterIfIntersects(other).ifPresent((Double parameter) -> ref.intersection = calculatePointAt(parameter));

        return Optional.ofNullable(ref.intersection);
    }

    public Optional<Point2D> findIntersection(Line2D line) {

        var ref = new Object() {
            Point2D intersection = null;
        };

        Line2D.from(this).findIntersection(line).ifPresent((Point2D intersection) -> {
            if (isPointOnRay(intersection)) {
                ref.intersection = intersection;
            }
        });

        return Optional.ofNullable(ref.intersection);
    }

    public Optional<Point2D> findIntersection(LineSegment2D ls) {

        var ref = new Object() {
            Point2D intersection = null;
        };

        Ray2D.from(ls).findIntersection(this).ifPresent((Point2D intersection) -> {
            if (ls.isPointOnLineSegment(intersection)) {
                ref.intersection = intersection;
            }
        });

        return Optional.ofNullable(ref.intersection);
    }

    public List<Point2D> findIntersection(Circle circle) {
        return circle.findIntersection(this);
    }


    //
    //             x  x
    //          x        x
    //         x    .     x
    //         x  Circle  x
    //          x        x
    //             x  x
    //
    //
    //    (*)----------------►
    //   Origin         Direction
    //

    public Point2D findClosestPointToCircleCenter(Circle circle) {
        Vector2D originToCenter = circle.getCenter().subtract(origin);
        double dot = direction.dot(originToCenter);

        if (dot <= 0.0) {
            // If dot is negative then the closest point is the origin.
            // In fact, if dot product is negative, then the point on the line passing through this ray that is
            // closest to circle lies on other direction of this ray.

            //             x  x
            //          x        x    Closest Point
            //         x    .     x ↙
            //         x          x         (*)----------------►
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

    public static Ray2D from(LineSegment2D ls) {
        Vector2D direction = new Vector2D(ls.getQ().x - ls.getP().x, ls.getQ().y - ls.getP().y);
        return new Ray2D(ls.getP(), direction);
    }

    public static Ray2D from(Line2D line) {
        Vector2D direction = new Vector2D(line.getQ().x - line.getP().x, line.getQ().y - line.getP().y);
        return new Ray2D(line.getP(), direction);
    }

}
