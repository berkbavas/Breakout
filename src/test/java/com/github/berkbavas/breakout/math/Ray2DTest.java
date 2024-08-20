package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Ray2DTest {

    @Test
    public void testPointCalculatePointAt() {
        Assert.assertEquals(new Point2D(2.123, 1.0), new Ray2D(new Point2D(1.0, 1.0), new Vector2D(1.0, 0.0)).calculate(1.123));
    }

    @Test
    public void testFindIntersectionRayLine() {
        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Line2D(new Point2D(0, 0), new Point2D(-1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, 1), new Vector2D(0, 1))
                .findIntersection(new Line2D(new Point2D(0, 0), new Point2D(-1, -1)))
                .ifPresent((p) -> Assert.fail());

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Line2D(new Point2D(1, 1), new Point2D(1, 2)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 1), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Line2D(new Point2D(0.5, 0.5), new Point2D(-1, 0.5)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0.5, 0.5), result), Assert::fail);

        new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0))
                .findIntersection(new Line2D(new Point2D(-1, -1), new Point2D(1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(1, 1), new Vector2D(0, -1))
                .findIntersection(new Line2D(new Point2D(-1, 0), new Point2D(1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Line2D(new Point2D(0, -1), new Point2D(1, 0)))
                .ifPresent((p) -> Assert.fail());

        new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1))
                .findIntersection(new Line2D(new Point2D(0, -1), new Point2D(1, 0)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(-100, -100), new Vector2D(1, 1))
                .findIntersection(new Line2D(new Point2D(0, -1), new Point2D(1, 0)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(0, -1), new Vector2D(0, 1))
                .findIntersection(new Line2D(new Point2D(0, -1), new Point2D(1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, -1), result), Assert::fail);
    }

    @Test
    public void testFindIntersectionBetweenTwoRays() {

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(0, 1), new Vector2D(1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 1), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(0, 0), new Vector2D(-1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(1, 1), new Vector2D(-1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 1), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 0))
                .findIntersection(new Ray2D(new Point2D(1, 1), new Vector2D(0, -1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(0, 1))
                .findIntersection(new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(0, 1))
                .findIntersection(new Ray2D(new Point2D(1, 1), new Vector2D(-1, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 1), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(0.5, 0.5), new Vector2D(-1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0.5, 0.5), result), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(-100, 0), new Vector2D(-1, -1)))
                .ifPresent(result -> Assert.fail());


        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findIntersection(new Ray2D(new Point2D(-100, 0), new Vector2D(-1, -1)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(1, 1), new Vector2D(1, 0))
                .findIntersection(new Ray2D(new Point2D(-1, -1), new Vector2D(-1, 0)))
                .ifPresent(result -> Assert.fail());
    }

    @Test
    public void testFindParameter() {
        new Ray2D(new Point2D(0, 0), new Vector2D(1, 0))
                .findParameterForGivenPoint(new Point2D(0, 1))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(1, 1), new Vector2D(1, 0))
                .findParameterForGivenPoint(new Point2D(0, 2))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 0))
                .findParameterForGivenPoint(new Point2D(100, 0))
                .ifPresentOrElse(result -> Assert.assertEquals(100, result, Util.EPSILON), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(1, 1))
                .findParameterForGivenPoint(new Point2D(100, 100))
                .ifPresentOrElse(result -> Assert.assertEquals(100 * Math.sqrt(2), result, Util.EPSILON), Assert::fail);

        new Ray2D(new Point2D(0, 0), new Vector2D(-1, 0))
                .findParameterForGivenPoint(new Point2D(100, 0))
                .ifPresentOrElse(result -> Assert.assertEquals(-100, result, Util.EPSILON), Assert::fail);

        new Ray2D(new Point2D(1, 1), new Vector2D(1, 1))
                .findParameterForGivenPoint(new Point2D(100, 100))
                .ifPresentOrElse(result -> Assert.assertEquals(99f * Math.sqrt(2.0), result, Util.EPSILON), Assert::fail);
    }

    @Test
    public void testIsCollinear() {
        Assert.assertTrue(new Ray2D(new Point2D(1, 1), new Vector2D(1, 1)).isCollinear(new Ray2D(new Point2D(-1, -1), new Vector2D(-1, -1))));
        Assert.assertTrue(new Ray2D(new Point2D(1, 0), new Vector2D(1, 0)).isCollinear(new Ray2D(new Point2D(-1, 0), new Vector2D(-1, 0))));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)).isCollinear(new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1))));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)).isCollinear(new Ray2D(new Point2D(1, 1), new Vector2D(1, 1))));
        Assert.assertFalse(new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)).isCollinear(new Ray2D(new Point2D(1, 0), new Vector2D(-1, -1))));
        Assert.assertFalse(new Ray2D(new Point2D(0, 0), new Vector2D(0, -1)).isCollinear(new Ray2D(new Point2D(1, 0), new Vector2D(0, -1))));
        Assert.assertFalse(new Ray2D(new Point2D(0, 0), new Vector2D(1, 2)).isCollinear(new Ray2D(new Point2D(1, 0), new Vector2D(2, -1))));
    }

    @Test
    public void testIsPointOnRay() {
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(0.5, 0.5)));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(0.0, 0.0)));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(1.0, 1.0)));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(200.0, 200.0)));
        Assert.assertFalse(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(1.00001, 1.0)));
        Assert.assertFalse(new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1)).isPointOnRay(new Point2D(1.01, 1.01)));
        Assert.assertTrue(new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1)).isPointOnRay(new Point2D(0, 0)));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(1, 0)).isPointOnRay(new Point2D(100, 0)));
        Assert.assertTrue(new Ray2D(new Point2D(0, 0), new Vector2D(0, 1)).isPointOnRay(new Point2D(0, 100)));
        Assert.assertFalse(new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).isPointOnRay(new Point2D(0, 0.1)));
    }

    @Test
    public void testFindClosestPointToCircleCenter() {
        Assert.assertEquals(new Point2D(0, 0), new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0.5, 0.5), new Ray2D(new Point2D(0.5, 0.5), new Vector2D(1, 1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0, 0), new Ray2D(new Point2D(0.5, 0.5), new Vector2D(-1, -1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(1, 0), new Ray2D(new Point2D(1, -1), new Vector2D(0, 1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(1, -1), new Ray2D(new Point2D(1, -1), new Vector2D(0, -1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(5, 5), new Ray2D(new Point2D(10, 0), new Vector2D(-1, 1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(10, 0), new Ray2D(new Point2D(10, -10), new Vector2D(0, 1)).findClosestPointToCenterOfCircle(Circle.UNIT_CIRCLE));
    }
}
