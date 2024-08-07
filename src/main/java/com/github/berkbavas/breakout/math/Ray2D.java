package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Ray2D {
    private final Point2D origin;
    private final Vector2D direction;

    public Ray2D(Point2D origin, Vector2D direction) {
        this.origin = origin;
        this.direction = direction.normalized();
    }

    public Point2D pointAt(float t) {
        final float x = origin.x + t * direction.x;
        final float y = origin.y + t * direction.y;

        return new Point2D(x, y);
    }

    public Optional<Float> findParameterFor(Point2D P) {
        // Px = Ox + t * Dx  => t = (Px - Ox) / Dx when Dx != 0
        // Py = Oy + t * Dy
        // Therefore we must have Py = Oy + ((Px - Ox) / Dx) * Dy

        // Both Dx and Dy may be zero
        // Dx may be zero and Dy may be nonzero
        // Dy may be zero and Dx may be nonzero
        // Both Dx and Dy may be nonzero

        Optional<Float> result = Optional.empty();

        if (Util.isFuzzyZero(direction.x) && Util.isFuzzyZero(direction.y)) { // Both Dx and Dy are zero
            if (Util.fuzzyCompare(P.x, origin.x) && Util.fuzzyCompare(P.y, origin.y)) {
                result = Optional.of(0.0f);
            }
        } else if (Util.isFuzzyZero(direction.x)) { // Dx is zero and Dy is nonzero
            if (Util.fuzzyCompare(P.x, origin.x)) {
                final float t = (P.y - origin.y) / direction.y;
                result = Optional.of(t);
            }
        } else if (Util.isFuzzyZero(direction.y)) { // Dy is zero and Dx is nonzero
            if (Util.fuzzyCompare(P.y, origin.y)) {
                final float t = (P.x - origin.x) / direction.x;
                result = Optional.of(t);
            }
        } else { // Both Dx and Dy are nonzero
            final float t = (P.x - origin.x) / direction.x; //  t = (Px - Ox) / Dx
            final float s = (P.y - origin.y) / direction.y; //  s = (Py - Oy) / Dy
            if (Util.fuzzyCompare(t, s)) {  // t and s must be equal
                result = Optional.of(t);
            }
        }

        return result;
    }

    public boolean isCollinear(Ray2D other) {
        Optional<Float> parameter = findParameterFor(other.origin);

        if (parameter.isEmpty()) {
            return false;
        }

        boolean sameDirection = direction.equals(other.direction);
        boolean oppositeDirection = direction.equals(other.direction.opposite());

        return sameDirection || oppositeDirection;
    }

    public Optional<Matrix2x1> findIntersection(Ray2D other) {
        // First check collinearity
        Optional<Float> parameter = findParameterFor(other.origin);

        if (parameter.isPresent()) {
            // They are collinear
            final float t = parameter.get();
            if (Util.isGreaterThanOrEqualToZero(t)) { // Check if we point to other ray's origin
                // <------o    <---------this
                return Optional.of(new Matrix2x1(t, 0.0f));
            } else {
                if (direction.equals(other.direction)) { // Check if other ray points to our origin

                    // <---------this      <--------o
                    return Optional.of(new Matrix2x1(0.0f, -t));
                }

                // <---------this    o--------->
                // Collinear but no intersection
            }
        }

        // If they are not collinear then we have
        // o0 + d0 * t = o1 + d1 * s  => d0 * t - d1 * s = o1 - o0
        //
        // | org1x - org0x |  | dir0x, -dir1x | | t |
        // | org1y - org0y |  | dir0y, -dir1y | | s |

        final float org0x = origin.x;
        final float org0y = origin.y;
        final float org1x = other.origin.x;
        final float org1y = other.origin.y;

        final float dir0x = direction.x;
        final float dir0y = direction.y;
        final float dir1x = other.direction.x;
        final float dir1y = other.direction.y;

        final Matrix2x1 lhs = new Matrix2x1(org1x - org0x, org1y - org0y);
        final Matrix2x2 rhs = new Matrix2x2(dir0x, -dir1x, dir0y, -dir1y);

        final Optional<Matrix2x1> solution = Matrix2x2.solve(lhs, rhs);

        if (solution.isPresent()) {
            final float t = solution.get().getM00();
            final float s = solution.get().getM10();

            if (Util.isGreaterThanOrEqualToZero(t) && Util.isGreaterThanOrEqualToZero(s)) {
                return solution;
            }
        }

        return Optional.empty();
    }

    public Optional<Point2D> findIntersectionPoint(Ray2D other) {
        Optional<Matrix2x1> intersection = findIntersection(other);

        if (intersection.isEmpty()) {
            return Optional.empty();
        }

        final float t = intersection.get().getM00();

        return Optional.of(pointAt(t));
    }

    @Override
    public String toString() {
        return String.format("Ray2D{origin = %s, direction = %s}", origin, direction);
    }
}
