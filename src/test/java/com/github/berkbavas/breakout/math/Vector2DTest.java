package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Vector2DTest {

    @Test
    public void testMultiplication() {
        Assert.assertEquals( new Vector2D(0.0f, 0.0f), new Vector2D(0.0f, 0.0f).multiply(0.0f));
        Assert.assertEquals( new Vector2D(0.0f, 0.0f), new Vector2D(1.0f, -2.0f).multiply(0.0f));
        Assert.assertEquals( new Vector2D(0.0f, 0.0f), new Vector2D(0.0f, 0.0f).multiply(1.0f));
        Assert.assertEquals( new Vector2D(-1.19f, 2.38f), new Vector2D(1.0f, -2.0f).multiply(-1.19f));
    }

    @Test
    public void testDotProduct() {
        Assert.assertEquals(0.0f, Vector2D.dot(new Vector2D(0.0f, 0.0f), new Vector2D(1.0f, 1.0f)), Util.EPSILON);
        Assert.assertEquals(2.0f, Vector2D.dot(new Vector2D(1.0f, 1.0f), new Vector2D(1.0f, 1.0f)), Util.EPSILON);
        Assert.assertEquals(0.0f, Vector2D.dot(new Vector2D(1.0f, 0.0f), new Vector2D(0.0f, 1.0f)), Util.EPSILON);
        Assert.assertEquals(0.0f, Vector2D.dot(new Vector2D(2.0f, -2.0f), new Vector2D(2.0f, 2.0f)), Util.EPSILON);
        Assert.assertEquals(0.0f, Vector2D.dot(new Vector2D(-2.0f, 2.0f), new Vector2D(2.0f, 2.0f)), Util.EPSILON);
        Assert.assertEquals(-8.0f, Vector2D.dot(new Vector2D(-2.0f, -2.0f), new Vector2D(2.0f, 2.0f)), Util.EPSILON);
        Assert.assertEquals(0.0f, Vector2D.dot(new Vector2D(-1.0f, 2.0f), new Vector2D(2.0f, 1.0f)), Util.EPSILON);
    }

    @Test
    public void testReflection() {
        Assert.assertEquals(new Vector2D(2.0f, 0.0f), new Vector2D(-2.0f, 0.0f).reflect(new Vector2D(1.0f, 0.0f)));
        Assert.assertEquals(new Vector2D(2.0f, 0.0f), new Vector2D(-2.0f, 0.0f).reflect(new Vector2D(1.0f, 0.0f)));
        Assert.assertEquals(new Vector2D(-2.0f, 0.0f), new Vector2D(2.0f, 0.0f).reflect(new Vector2D(-1.0f, 0.0f)));
        Assert.assertEquals(new Vector2D(-2.0f, 1.0f), new Vector2D(2.0f, 1.0f).reflect(new Vector2D(-1.0f, 0.0f)));

        Assert.assertEquals(new Vector2D(0.0f, 2.0f), new Vector2D(0.0f, -2.0f).reflect(new Vector2D(0.0f, 1.0f)));
        Assert.assertEquals(new Vector2D(1.0f, 2.0f), new Vector2D(1.0f, -2.0f).reflect(new Vector2D(0.0f, 1.0f)));
        Assert.assertEquals(new Vector2D(0.0f, -2.0f), new Vector2D(0.0f, 2.0f).reflect(new Vector2D(0.0f, -1.0f)));
        Assert.assertEquals(new Vector2D(1.0f, -2.0f), new Vector2D(1.0f, 2.0f).reflect(new Vector2D(0.0f, -1.0f)));

        Assert.assertEquals(new Vector2D(1.0f, -1.0f), new Vector2D(-1.0f, 1.0f).reflect(new Vector2D(1.0f, -1.0f)));
        Assert.assertEquals(new Vector2D(0.0f, -1.0f), new Vector2D(-1.0f, 0.0f).reflect(new Vector2D(1.0f, -1.0f)));

        Assert.assertEquals(new Vector2D(-1.0f, 1.0f), new Vector2D(1.0f, -1.0f).reflect(new Vector2D(-1.0f, 1.0f)));
        Assert.assertEquals(new Vector2D(0.0f, 1.0f), new Vector2D(1.0f, 0.0f).reflect(new Vector2D(-1.0f, 1.0f)));

        Assert.assertEquals(new Vector2D(1.0f, 0.0f), new Vector2D(0.0f, -1.0f).reflect(new Vector2D(1.0f, 1.0f)));
        Assert.assertEquals(new Vector2D(1.0f, 1.0f), new Vector2D(-1.0f, -1.0f).reflect(new Vector2D(1.0f, 1.0f)));

        Assert.assertEquals(new Vector2D(-1.0f, 0.0f), new Vector2D(0.0f, 1.0f).reflect(new Vector2D(-1.0f, -1.0f)));
        Assert.assertEquals(new Vector2D(-1.0f, -1.0f), new Vector2D(1.0f, 1.0f).reflect(new Vector2D(-1.0f, -1.0f)));

    }

    @Test
    public void testNormalization() {
        float inverseSqrt2 = 1.0f / (float) Math.sqrt(2.0f);
        Assert.assertEquals(new Vector2D(1.0f, 0.0f), new Vector2D(1.0f, 0.0f).normalized());

        Assert.assertEquals(new Vector2D(1.0f, 1.0f).multiply(inverseSqrt2), new Vector2D(1.0f, 1.0f).normalized());
        Assert.assertEquals(new Vector2D(-1.0f, 1.0f).multiply(inverseSqrt2), new Vector2D(-1.0f, 1.0f).normalized());
        Assert.assertEquals(new Vector2D(-1.0f, -1.0f).multiply(inverseSqrt2), new Vector2D(-1.0f, -1.0f).normalized());
        Assert.assertEquals(new Vector2D(1.0f, -1.0f).multiply(inverseSqrt2), new Vector2D(1.0f, -1.0f).normalized());
    }

    @Test
    public void testOpposite() {
        Assert.assertEquals(new Vector2D(0.0f, 0.0f), new Vector2D(0.0f, 0.0f).opposite());
        Assert.assertEquals(new Vector2D(-1.0f, 0.0f), new Vector2D(1.0f, 0.0f).opposite());
        Assert.assertEquals(new Vector2D(0.0f, 10.0f), new Vector2D(0.0f, -10.0f).opposite());
        Assert.assertEquals(new Vector2D(-1.0f, 10.0f), new Vector2D(1.0f, -10.0f).opposite());
    }
}
