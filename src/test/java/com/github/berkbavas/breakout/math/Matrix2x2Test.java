package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class Matrix2x2Test {
    private static final float EPSILON = 1e-8f;

    @Test
    public void testDeterminant() {
        final float m00 = -1.13f;
        final float m01 = -3.58f;
        final float m10 = 3.5f;
        final float m11 = 21.24f;

        final Matrix2x2 matrix = new Matrix2x2(m00, m01, m10, m11);

        final float actual = matrix.determinant();
        final float expected = m00 * m11 - m01 * m10;

        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testMultiply() {
        final float m00 = 10.43f;
        final float m01 = -3.58f;
        final float m10 = 34.5f;
        final float m11 = 201.24f;

        final float n00 = 2.44f;
        final float n10 = 1.01f;

        final Matrix2x2 M = new Matrix2x2(m00, m01, m10, m11);
        final Matrix2x1 N = new Matrix2x1(n00, n10);

        final Matrix2x1 actual = M.multiply(N);
        final Matrix2x1 expected = new Matrix2x1(m00 * n00 + m01 * n10, m10 * n00 + m11 * n10);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInvertedSingularMatrix() {
        final float m00 = -1.3f;
        final float m01 = 0.0f;
        final float m10 = 3.33f;
        final float m11 = 0.0f;

        final Matrix2x2 singular = new Matrix2x2(m00, m01, m10, m11);
        final float actual = singular.determinant();

        Assert.assertEquals(0.0f, actual, EPSILON);
        Assert.assertTrue(singular.inverted().isEmpty());
    }

    @Test
    public void testInverted() {
        final float m00 = 10.43f;
        final float m01 = -3.58f;
        final float m10 = 34.5f;
        final float m11 = 201.24f;

        final Matrix2x2 matrix = new Matrix2x2(m00, m01, m10, m11);

        final Optional<Matrix2x2> inverse = matrix.inverted();

        Assert.assertTrue(inverse.isPresent());

        final Matrix2x2 actual = inverse.get();
        final Matrix2x2 expected = new Matrix2x2(0.090548995807856866712f, 0.0016108398180884892806f,
                -0.01552345634750080452f, 0.0046930333247661852507f);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSolveSingularMatrix() {
        final Matrix2x1 A = new Matrix2x1(1.0f, 2.0f);
        final Matrix2x2 B = new Matrix2x2(0.0f, 1.0f, 0.0f, 0.0f);
        Assert.assertTrue(Matrix2x2.solve(A, B).isEmpty());
    }

    @Test
    public void testSolve() {
        final Matrix2x1 A = new Matrix2x1(1.0f, 2.0f);
        final Matrix2x2 B = new Matrix2x2(1.3f, 10.01f, -4.5f, 1.0f);
        final Optional<Matrix2x1> solution = Matrix2x2.solve(A, B);

        Assert.assertTrue(solution.isPresent());

        final Matrix2x1 actual = solution.get();
        final Matrix2x1 expected = new Matrix2x1(-0.41040027f, 0.15319884f);

        Assert.assertEquals(expected, actual);
    }
}
