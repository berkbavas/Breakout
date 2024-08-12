package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class Matrix2x2Test {
    private static final double EPSILON = 1e-10;

    @Test
    public void testDeterminant() {
        final double m00 = -1.13;
        final double m01 = -3.58;
        final double m10 = 3.5;
        final double m11 = 21.24;

        final Matrix2x2 matrix = new Matrix2x2(m00, m01, m10, m11);

        final double actual = matrix.determinant();
        final double expected = m00 * m11 - m01 * m10;

        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testMultiply() {
        final double m00 = 10.43;
        final double m01 = -3.58;
        final double m10 = 34.5;
        final double m11 = 201.24;

        final double n00 = 2.44;
        final double n10 = 1.01;

        final Matrix2x2 M = new Matrix2x2(m00, m01, m10, m11);
        final Matrix2x1 N = new Matrix2x1(n00, n10);

        final Matrix2x1 actual = M.multiply(N);
        final Matrix2x1 expected = new Matrix2x1(m00 * n00 + m01 * n10, m10 * n00 + m11 * n10);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInvertedSingularMatrix() {
        final double m00 = -1.3;
        final double m01 = 0.0;
        final double m10 = 3.33;
        final double m11 = 0.0;

        final Matrix2x2 singular = new Matrix2x2(m00, m01, m10, m11);
        final double actual = singular.determinant();

        Assert.assertEquals(0.0, actual, EPSILON);
        Assert.assertTrue(singular.inverted().isEmpty());
    }

    @Test
    public void testInverted() {
        final double m00 = 10.43;
        final double m01 = -3.58;
        final double m10 = 34.5;
        final double m11 = 201.24;

        final Matrix2x2 matrix = new Matrix2x2(m00, m01, m10, m11);

        final Optional<Matrix2x2> inverse = matrix.inverted();

        Assert.assertTrue(inverse.isPresent());

        final Matrix2x2 actual = inverse.get();
        final Matrix2x2 expected = new Matrix2x2(
                0.090548995807856866712, 0.0016108398180884892806,
                -0.01552345634750080452, 0.0046930333247661852507);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSolveSingularMatrix() {
        final Matrix2x1 A = new Matrix2x1(1.0, 2.0);
        final Matrix2x2 B = new Matrix2x2(0.0, 1.0, 0.0, 0.0);
        Assert.assertTrue(Matrix2x2.solve(A, B).isEmpty());
    }

    @Test
    public void testSolve() {
        final Matrix2x1 A = new Matrix2x1(1.0, 2.0);
        final Matrix2x2 B = new Matrix2x2(1.3, 10.01, -4.5, 1.0);
        final Optional<Matrix2x1> solution = Matrix2x2.solve(A, B);

        Assert.assertTrue(solution.isPresent());

        final Matrix2x1 actual = solution.get();
        final Matrix2x1 expected = new Matrix2x1(-0.4104002589276081, 0.1531988348257633);

        Assert.assertEquals(expected, actual);
    }
}
