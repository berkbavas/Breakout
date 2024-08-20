package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Point2DTest {

    @Test
    public void testAddition() {
        Assert.assertEquals(new Point2D(0.0, 0.0),
                new Point2D(1.01f, 2.0005).add(new Point2D(-1.01f, -2.0005)));
    }

    @Test
    public void testSubtraction() {
        Assert.assertEquals(new Point2D(0.0, 0.0),
                new Point2D(10.1f, 20.10001).subtract(new Point2D(10.1f, 20.10001)).toPoint2D());
    }

    @Test
    public void testDistanceBetween() {
        Assert.assertEquals(5.0, Point2D.distanceBetween(new Point2D(3.0, 4.0), new Point2D(0.0, 0.0)), Util.EPSILON);
    }

    @Test
    public void testDistanceTo() {
        Assert.assertEquals(5.0, new Point2D(-3.0, -4.0).distanceTo(new Point2D(0.0, 0.0)), Util.EPSILON);
    }
}
