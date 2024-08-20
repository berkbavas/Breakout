package com.github.berkbavas.breakout.math;

import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@ToString
@Getter
public class Matrix2x2 {
    private final double m00;
    private final double m01;
    private final double m10;
    private final double m11;

    public Matrix2x2(double m00, double m01, double m10, double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    public double determinant() {
        return m00 * m11 - m01 * m10;
    }

    public Matrix2x1 multiply(Matrix2x1 other) {
        final double r0 = m00 * other.getM00() + m01 * other.getM10();
        final double r1 = m10 * other.getM00() + m11 * other.getM10();

        return new Matrix2x1(r0, r1);
    }

    public Optional<Matrix2x2> inverted() {
        final double det = determinant();

        if (Util.isFuzzyZero(det)) {
            return Optional.empty();
        }

        final double n00 = m11 / det;
        final double n01 = m01 / det;
        final double n10 = m10 / det;
        final double n11 = m00 / det;

        return Optional.of(new Matrix2x2(n00, -n01, -n10, n11));
    }

    // For given 2x2 matrix A and 2x1 matrix B, finds the solution for A*S = B
    // and returns S if such solution exists.
    public static Optional<Matrix2x1> solve(Matrix2x2 A, Matrix2x1 B) {
        // Find A^-1 if it exists
        // S = A^-1 * B
        return A.inverted().map(inverse -> inverse.multiply(B));
    }

    // Solves [A0,B0;A1,B1] * [x;y] + [C0;C1] = [0;0] for (x, y).
    public static Optional<Point2D> solve(double A0, double B0, double C0, double A1, double B1, double C1) {
        Matrix2x2 lhs = new Matrix2x2(A0, B0, A1, B1);
        Matrix2x1 rhs = new Matrix2x1(-C0, -C1);

        return Matrix2x2.solve(lhs, rhs).map((solution) -> new Point2D(solution.getM00(), solution.getM10()));
    }

    public static Optional<Point2D> solve(Line2D l0, Line2D l1) {
        return solve(l0.getA(), l0.getB(), l0.getC(), l1.getA(), l1.getB(), l1.getC());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Matrix2x2) {
            final Matrix2x2 other = (Matrix2x2) object;

            return Util.fuzzyCompare(m00, other.m00) &&
                    Util.fuzzyCompare(m01, other.m01) &&
                    Util.fuzzyCompare(m10, other.m10) &&
                    Util.fuzzyCompare(m11, other.m11);
        } else {
            return false;
        }
    }
}
