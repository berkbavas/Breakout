package com.github.berkbavas.breakout.math;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Optional;

@Getter
public class LineSegment2D {
    private final Point2D P;
    private final Point2D Q;
    private final double length;
    private final String identifier;
    private final Vector2D direction;

    @Getter(AccessLevel.NONE)
    private final HashMap<NormalOrientation, Vector2D> normals = new HashMap<>();

    // These are the coefficients of the equation of the line passing through this line segment.
    // We cache these for the sake of fast intersection calculations.
    private final double A;
    private final double B;
    private final double C;

    public enum NormalOrientation {
        INWARDS,
        OUTWARDS
    }

    public LineSegment2D(Point2D P, Point2D Q) {
        this(P, Q, "");
    }

    public LineSegment2D(Point2D P, Point2D Q, String identifier) {
        this.P = P;
        this.Q = Q;
        this.length = P.distanceTo(Q);
        this.identifier = identifier;

        Double[] coefficients = Line2D.calculateEquationCoefficients(P, Q);

        this.A = coefficients[0];
        this.B = coefficients[1];
        this.C = coefficients[2];

        this.direction = Q.subtract(P);

        constructNormals();
    }

    public boolean isPointOnLineSegment(Point2D point) {
        if (point.equals(P) || point.equals(Q)) {
            return true;
        }

        double totalDistance = point.distanceTo(P) + point.distanceTo(Q);
        return Util.fuzzyCompare(length, totalDistance);
    }

    public Point2D getClosestVertexToPoint(Point2D point) {
        double d0 = point.distanceTo(P);
        double d1 = point.distanceTo(Q);

        return d0 < d1 ? P : Q;
    }

    public Vector2D getNormal(NormalOrientation normalOrientation) {
        return normals.get(normalOrientation);
    }

    public Optional<Point2D> findIntersection(LineSegment2D other) {

        //
        //
        //          * Q1
        //           \
        //            \
        //             \
        //         *----*----------------------*
        //         P0    \                     Q0
        //                \
        //                 \
        //                  * P1
        //
        //

        if (isPointOnLineSegment(other.P)) {
            return Optional.of(other.P);
        } else if (isPointOnLineSegment(other.Q)) {
            return Optional.of(other.Q);
        } else if (other.isPointOnLineSegment(P)) {
            return Optional.of(P);
        } else if (other.isPointOnLineSegment(Q)) {
            return Optional.of(Q);
        }

        return Matrix2x2.solve(A, B, C, other.A, other.B, other.C).map((intersection) -> {
            if (isPointOnLineSegment(intersection) && other.isPointOnLineSegment(intersection)) {
                return intersection;
            }
            return null;
        });
    }

    @Override
    public String toString() {
        return String.format("LineSegment2D%-10s : {P = %s, Q = %s}", identifier, P, Q);
    }

    private void constructNormals() {
        final double dx = Q.getX() - P.getX();
        final double dy = Q.getY() - P.getY();

        normals.put(NormalOrientation.OUTWARDS, new Vector2D(-dy, dx).normalized());
        normals.put(NormalOrientation.INWARDS, new Vector2D(dy, -dx).normalized());
    }
}
