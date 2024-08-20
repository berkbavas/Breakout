package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class LineSegment2DTest {

    @Test
    public void testFindIntersectionBetweenRayAndLineSegment() {
        new Ray2D(new Point2D(1, 1), new Vector2D(0, -1))
                .findIntersection(new LineSegment2D(new Point2D(0, 0), new Point2D(13.3, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 0), result), Assert::fail);

        new Ray2D(new Point2D(1, 1), new Vector2D(0, 1))
                .findIntersection(new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(100, -1), new Vector2D(1, -1))
                .findIntersection(new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(2, 0), new Vector2D(1, 1))
                .findIntersection(new LineSegment2D(new Point2D(0, 0), new Point2D(1, 0)))
                .ifPresent(result -> Assert.fail());

        new Ray2D(new Point2D(-1, 1), new Vector2D(1, -1))
                .findIntersection(new LineSegment2D(new Point2D(1, 1), new Point2D(-1, -1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(1, -1), new Vector2D(-1, 1))
                .findIntersection(new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0))
                .findIntersection(new LineSegment2D(new Point2D(0, -100), new Point2D(0, 100)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new Ray2D(new Point2D(0, -10), new Vector2D(0, 1))
                .findIntersection(new LineSegment2D(new Point2D(100, 0), new Point2D(-100, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

    }

    @Test
    public void testIsPointOnLineSegment() {
        LineSegment2D ls = new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1));
        Assert.assertTrue(ls.isPointOnLineSegment(new Point2D(0, 0)));
        Assert.assertTrue(ls.isPointOnLineSegment(new Point2D(-1, -1)));
        Assert.assertTrue(ls.isPointOnLineSegment(new Point2D(1, 1)));
        Assert.assertFalse(ls.isPointOnLineSegment(new Point2D(-2, 0)));
        Assert.assertFalse(ls.isPointOnLineSegment(new Point2D(-1 - 0.001, -1)));
        Assert.assertFalse(ls.isPointOnLineSegment(new Point2D(-1 - 0.0001, -1 - 0.0001)));
        Assert.assertFalse(ls.isPointOnLineSegment(new Point2D(1 + 0.0001, 1 + 0.0001)));
    }

    @Test
    public void testFindIntersectionBetweenTwoLineSegments() {
        new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0))
                .findIntersection(new LineSegment2D(new Point2D(0, 1), new Point2D(0, -1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0))
                .findIntersection(new LineSegment2D(new Point2D(1, 0), new Point2D(2, 0)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 0), result), Assert::fail);

        new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0))
                .findIntersection(new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0, 0), result), Assert::fail);

        new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1))
                .findIntersection(new LineSegment2D(new Point2D(1, 0), new Point2D(1, 10)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(1, 1), result), Assert::fail);

        new LineSegment2D(new Point2D(-1, 1), new Point2D(0, 1))
                .findIntersection(new LineSegment2D(new Point2D(0, 2), new Point2D(1, 2)))
                .ifPresent(result -> Assert.fail());

        new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1))
                .findIntersection(new LineSegment2D(new Point2D(1, 0), new Point2D(-1, 2)))
                .ifPresentOrElse(result -> Assert.assertEquals(new Point2D(0.5, 0.5), result), Assert::fail);

        new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0))
                .findIntersection(new LineSegment2D(new Point2D(4, 0), new Point2D(2, 0)))
                .ifPresent(result -> Assert.fail());

        new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0))
                .findIntersection(new LineSegment2D(new Point2D(2, 0), new Point2D(4, 0)))
                .ifPresent(result -> Assert.fail());

    }
}
