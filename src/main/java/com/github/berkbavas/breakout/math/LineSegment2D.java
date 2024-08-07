package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.Optional;

@Getter
public class LineSegment2D {

    private final Point2D P;
    private final Point2D Q;

    public LineSegment2D(Point2D P, Point2D Q) {
        this.P = P;
        this.Q = Q;
    }

    public Optional<Point2D> findIntersection(Ray2D other) {
        // Ray(t) = P + t * (Q - P)
        // Origin is P.
        // Direction is (Q - P).

        Vector2D direction = Q.subtract(P).toVector2D();
        Vector2D normalized = direction.normalized();
        Ray2D ray = new Ray2D(P, normalized);
        Optional<Matrix2x1> result = ray.findIntersection(other);

        if (result.isPresent()) {
            float norm = direction.norm();
            float t = result.get().getM00();
            if (Util.isFuzzyBetween(0.0f, t, norm)) {
                return Optional.of(ray.pointAt(t));
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return String.format("LineSegment2D{P = %s, Q = %s}", P, Q);
    }
}
