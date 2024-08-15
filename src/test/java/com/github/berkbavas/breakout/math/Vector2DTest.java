package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector2DTest {

    @Test
    public void testMultiplication() {
        Assert.assertEquals( new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0).multiply(0.0));
        Assert.assertEquals( new Vector2D(0.0, 0.0), new Vector2D(1.0, -2.0).multiply(0.0));
        Assert.assertEquals( new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0).multiply(1.0));
        Assert.assertEquals( new Vector2D(-1.19f, 2.38f), new Vector2D(1.0, -2.0).multiply(-1.19f));
    }

    @Test
    public void testDotProduct() {
        Assert.assertEquals(0.0, Vector2D.dot(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)), Util.EPSILON);
        Assert.assertEquals(2.0, Vector2D.dot(new Vector2D(1.0, 1.0), new Vector2D(1.0, 1.0)), Util.EPSILON);
        Assert.assertEquals(0.0, Vector2D.dot(new Vector2D(1.0, 0.0), new Vector2D(0.0, 1.0)), Util.EPSILON);
        Assert.assertEquals(0.0, Vector2D.dot(new Vector2D(2.0, -2.0), new Vector2D(2.0, 2.0)), Util.EPSILON);
        Assert.assertEquals(0.0, Vector2D.dot(new Vector2D(-2.0, 2.0), new Vector2D(2.0, 2.0)), Util.EPSILON);
        Assert.assertEquals(-8.0, Vector2D.dot(new Vector2D(-2.0, -2.0), new Vector2D(2.0, 2.0)), Util.EPSILON);
        Assert.assertEquals(0.0, Vector2D.dot(new Vector2D(-1.0, 2.0), new Vector2D(2.0, 1.0)), Util.EPSILON);
    }

    @Test
    public void testReflection() {
        Assert.assertEquals(new Vector2D(2.0, 0.0), new Vector2D(-2.0, 0.0).reflect(new Vector2D(1.0, 0.0)));
        Assert.assertEquals(new Vector2D(2.0, 0.0), new Vector2D(-2.0, 0.0).reflect(new Vector2D(1.0, 0.0)));
        Assert.assertEquals(new Vector2D(-2.0, 0.0), new Vector2D(2.0, 0.0).reflect(new Vector2D(-1.0, 0.0)));
        Assert.assertEquals(new Vector2D(-2.0, 1.0), new Vector2D(2.0, 1.0).reflect(new Vector2D(-1.0, 0.0)));

        Assert.assertEquals(new Vector2D(0.0, 2.0), new Vector2D(0.0, -2.0).reflect(new Vector2D(0.0, 1.0)));
        Assert.assertEquals(new Vector2D(1.0, 2.0), new Vector2D(1.0, -2.0).reflect(new Vector2D(0.0, 1.0)));
        Assert.assertEquals(new Vector2D(0.0, -2.0), new Vector2D(0.0, 2.0).reflect(new Vector2D(0.0, -1.0)));
        Assert.assertEquals(new Vector2D(1.0, -2.0), new Vector2D(1.0, 2.0).reflect(new Vector2D(0.0, -1.0)));

        Assert.assertEquals(new Vector2D(1.0, -1.0), new Vector2D(-1.0, 1.0).reflect(new Vector2D(1.0, -1.0)));
        Assert.assertEquals(new Vector2D(0.0, -1.0), new Vector2D(-1.0, 0.0).reflect(new Vector2D(1.0, -1.0)));

        Assert.assertEquals(new Vector2D(-1.0, 1.0), new Vector2D(1.0, -1.0).reflect(new Vector2D(-1.0, 1.0)));
        Assert.assertEquals(new Vector2D(0.0, 1.0), new Vector2D(1.0, 0.0).reflect(new Vector2D(-1.0, 1.0)));

        Assert.assertEquals(new Vector2D(1.0, 0.0), new Vector2D(0.0, -1.0).reflect(new Vector2D(1.0, 1.0)));
        Assert.assertEquals(new Vector2D(1.0, 1.0), new Vector2D(-1.0, -1.0).reflect(new Vector2D(1.0, 1.0)));

        Assert.assertEquals(new Vector2D(-1.0, 0.0), new Vector2D(0.0, 1.0).reflect(new Vector2D(-1.0, -1.0)));
        Assert.assertEquals(new Vector2D(-1.0, -1.0), new Vector2D(1.0, 1.0).reflect(new Vector2D(-1.0, -1.0)));

    }

    @Test
    public void testNormalization() {
        double inverseSqrt2 = 1.0 / (double) Math.sqrt(2.0);
        Assert.assertEquals(new Vector2D(1.0, 0.0), new Vector2D(1.0, 0.0).normalized());

        Assert.assertEquals(new Vector2D(1.0, 1.0).multiply(inverseSqrt2), new Vector2D(1.0, 1.0).normalized());
        Assert.assertEquals(new Vector2D(-1.0, 1.0).multiply(inverseSqrt2), new Vector2D(-1.0, 1.0).normalized());
        Assert.assertEquals(new Vector2D(-1.0, -1.0).multiply(inverseSqrt2), new Vector2D(-1.0, -1.0).normalized());
        Assert.assertEquals(new Vector2D(1.0, -1.0).multiply(inverseSqrt2), new Vector2D(1.0, -1.0).normalized());
    }

    @Test
    public void testReversed() {
        Assert.assertEquals(new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0).reversed());
        Assert.assertEquals(new Vector2D(-1.0, 0.0), new Vector2D(1.0, 0.0).reversed());
        Assert.assertEquals(new Vector2D(0.0, 10.0), new Vector2D(0.0, -10.0).reversed());
        Assert.assertEquals(new Vector2D(-1.0, 10.0), new Vector2D(1.0, -10.0).reversed());
    }
}
