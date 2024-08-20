package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Line2DTest {

    @Test
    public void testIsPointOnLine() {
        Assert.assertTrue(new Line2D(new Point2D(0, 0), new Point2D(1, 1)).isPointOnLine(new Point2D(0, 0)));
        Assert.assertTrue(new Line2D(new Point2D(0, 0), new Point2D(1, 1)).isPointOnLine(new Point2D(1, 1)));
        Assert.assertTrue(new Line2D(new Point2D(0, 0), new Point2D(1, 1)).isPointOnLine(new Point2D(1000, 1000)));
        Assert.assertTrue(new Line2D(new Point2D(-1, -1), new Point2D(1, 1)).isPointOnLine(new Point2D(0, 0)));
        Assert.assertFalse(new Line2D(new Point2D(-1, -1), new Point2D(1, 1)).isPointOnLine(new Point2D(1, 0)));
        Assert.assertFalse(new Line2D(new Point2D(0, -1), new Point2D(1, 0)).isPointOnLine(new Point2D(0, 0)));
        Assert.assertTrue(new Line2D(new Point2D(0, -1), new Point2D(1, 0)).isPointOnLine(new Point2D(0.5, -0.5)));
        Assert.assertTrue(new Line2D(new Point2D(0, 0), new Point2D(0, 1)).isPointOnLine(new Point2D(0, -0.5)));
        Assert.assertTrue(new Line2D(new Point2D(0, 1), new Point2D(0, 0)).isPointOnLine(new Point2D(0, -0.5)));
        Assert.assertTrue(new Line2D(new Point2D(0, 1), new Point2D(0, 0)).isPointOnLine(new Point2D(0, 1)));
        Assert.assertTrue(new Line2D(new Point2D(0, 1), new Point2D(0, 0)).isPointOnLine(new Point2D(0, 0)));
    }

    @Test
    public void testFindIntersectionWithLine() {
        new Line2D(new Point2D(0, 0), new Point2D(1, 1))
                .findIntersection(new Line2D(new Point2D(1, 0), new Point2D(1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 1), result), Assert::fail);

        new Line2D(new Point2D(0, 0), new Point2D(1, 1))
                .findIntersection(new Line2D(new Point2D(1, 0), new Point2D(2, 1)))
                .ifPresent(result -> Assert.fail());

        new Line2D(new Point2D(0, 0), new Point2D(1, 1))
                .findIntersection(new Line2D(new Point2D(0, 0), new Point2D(-1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Line2D(new Point2D(0, 0), new Point2D(1, 1))
                .findIntersection(new Line2D(new Point2D(1, -1), new Point2D(-1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Line2D(new Point2D(1, 0), new Point2D(2, 0))
                .findIntersection(new Line2D(new Point2D(1, -1), new Point2D(2, -1)))
                .ifPresent(result -> Assert.fail());
    }

    @Test
    public void testCalculateDistanceBetweenPointAndLine() {
        Assert.assertEquals(0, new Line2D(new Point2D(0, 0), new Point2D(1, 1)).calculateDistanceToPoint(new Point2D(0, 0)), Util.EPSILON);
        Assert.assertEquals(0.5 * Math.sqrt(2), new Line2D(new Point2D(0, 0), new Point2D(1, 1)).calculateDistanceToPoint(new Point2D(1, 0)), Util.EPSILON);
        Assert.assertEquals(0.5 * Math.sqrt(2), new Line2D(new Point2D(0, 0), new Point2D(1, 1)).calculateDistanceToPoint(new Point2D(0, 1)), Util.EPSILON);
        Assert.assertEquals(0, new Line2D(new Point2D(0, 0), new Point2D(1, 1)).calculateDistanceToPoint(new Point2D(1, 1)), Util.EPSILON);
        Assert.assertEquals(1, new Line2D(new Point2D(-1, -1), new Point2D(1, 1)).calculateDistanceToPoint(new Point2D(0, -Math.sqrt(2))), Util.EPSILON);
        Assert.assertEquals(0, new Line2D(new Point2D(0, -1), new Point2D(0, 1)).calculateDistanceToPoint(new Point2D(0, 0)), Util.EPSILON);
        Assert.assertEquals(0, new Line2D(new Point2D(0, -1), new Point2D(0, 1)).calculateDistanceToPoint(new Point2D(0, 10)), Util.EPSILON);
        Assert.assertEquals(10, new Line2D(new Point2D(0, -1), new Point2D(0, 1)).calculateDistanceToPoint(new Point2D(10, 0)), Util.EPSILON);
    }

    @Test
    public void testFindClosestPointToCircleCenter() {
        Assert.assertEquals(new Point2D(0, 0), new Line2D(new Point2D(0, -1), new Point2D(0, 1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0, 0), new Line2D(new Point2D(-1, -1), new Point2D(1, 1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0, 100), new Line2D(new Point2D(-1, 100), new Point2D(1, 100)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(1, 0), new Line2D(new Point2D(1, -1), new Point2D(1, 1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0.5, 0), new Line2D(new Point2D(0.5, -1), new Point2D(0.5, 1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0.5, 0.5), new Line2D(new Point2D(1, 0), new Point2D(0, 1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
    }

}
