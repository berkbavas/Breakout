package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Point2DTest {

    @Test
    public void testAddition() {
        Point2D p1 = new Point2D(1.01f, 2.0005);
        Point2D p2 = new Point2D(-1.01f, -2.0005);

        Point2D actual = p1.add(p2);
        Point2D expected = new Point2D(0.0, 0.0);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubtraction() {
        Point2D p1 = new Point2D(10.1f, 20.10001);
        Point2D p2 = new Point2D(10.1f, 20.10001);

        Point2D actual = p1.subtract(p2);
        Point2D expected = new Point2D(0.0, 0.0);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDistanceBetween() {
        Point2D p1 = new Point2D(3.0, 4.0);
        Point2D p2 = new Point2D(0.0, 0.0);

        double actual = Point2D.distanceBetween(p1, p2);
        double expected = 5.0;

        Assert.assertEquals(expected, actual, Util.EPSILON);
    }

    @Test
    public void testDistanceTo() {
        Point2D p1 = new Point2D(-3.0, -4.0);
        Point2D p2 = new Point2D(0.0, 0.0);

        double actual = p1.distanceTo(p2);
        double expected = 5.0;

        Assert.assertEquals(expected, actual, Util.EPSILON);
    }

    @Test
    public void testToVector2D() {
        Point2D point = new Point2D(0.0, 0.0);
        Vector2D vector = point.toVector2D();

        Assert.assertEquals(new Vector2D(0.0, 0.0), vector);
    }
}
