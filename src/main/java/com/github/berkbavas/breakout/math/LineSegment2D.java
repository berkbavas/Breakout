package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.HashMap;
import java.util.Optional;

@Getter
public class LineSegment2D {
    private final Point2D P;
    private final Point2D Q;
    private final Vector2D direction;
    private final double length;
    private final HashMap<NormalOrientation, Vector2D> normals = new HashMap<>();

    public enum NormalOrientation {
        INWARDS,
        OUTWARDS
    }

    public LineSegment2D(Point2D P, Point2D Q) {
        this.P = P;
        this.Q = Q;
        direction = new Vector2D(Q.x - P.x, Q.y - P.y);
        length = P.distanceTo(Q);

        constructNormals();
    }

    public boolean isPointOnLineSegment(Point2D point) {
        double length = getLength();
        double distance = P.distanceTo(point) + Q.distanceTo(point);

        if (Util.fuzzyCompare(length, distance)) {
            Vector2D expectedDirection = new Vector2D(point.x - P.x, point.y - P.y);
            Vector2D direction = getDirection();
            return direction.isCollinear(expectedDirection);
        } else {
            return false;
        }
    }

    public Vector2D getNormal(NormalOrientation normalOrientation) {
        return normals.get(normalOrientation);
    }

    public Optional<Point2D> findIntersection(LineSegment2D other) {

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

        final Point2D origin0 = P;
        final Point2D origin1 = other.P;

        final Vector2D dir0 = new Vector2D(Q.x - P.x, Q.y - P.y);
        final Vector2D dir1 = new Vector2D(other.Q.x - other.P.x, other.Q.y - other.P.y);

        final Matrix2x1 lhs = new Matrix2x1(origin1.x - origin0.x, origin1.y - origin0.y);
        final Matrix2x2 rhs = new Matrix2x2(dir0.x, -dir1.x, dir0.y, -dir1.y);

        final Optional<Matrix2x1> solution = Matrix2x2.solve(lhs, rhs);

        if (solution.isPresent()) {
            final double t = solution.get().getM00();
            if (Util.isFuzzyBetween(0.0, t, 1.0)) {
                return Optional.of(origin0.add(dir0.multiply(t)));
            }
        } else {
            if (isPointOnLineSegment(other.P)) {
                return Optional.of(other.P);
            } else if (isPointOnLineSegment(other.Q)) {
                return Optional.of(other.Q);
            } else if (other.isPointOnLineSegment(P)) {
                return Optional.of(other.P);
            } else if (other.isPointOnLineSegment(Q)) {
                return Optional.of(other.Q);
            }
        }

        return Optional.empty();
    }

    public Optional<Point2D> findIntersection(Ray2D ray) {
        return ray.findIntersection(this);
    }

    @Override
    public String toString() {
        return String.format("LineSegment2D{P = %s, Q = %s}", P, Q);
    }

    private void constructNormals() {
        final double dx = Q.x - P.x;
        final double dy = Q.y - P.y;

        normals.put(NormalOrientation.INWARDS, new Vector2D(-dy, dx).normalized());
        normals.put(NormalOrientation.OUTWARDS, new Vector2D(dy, -dx).normalized());
    }
}
