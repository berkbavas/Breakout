package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class LineSegment2DTest {

    public void findIntersection(LineSegment2D ls, Ray2D ray, Point2D expected) {
        Optional<Point2D> actual = ls.findIntersection(ray);

        if (expected == null) {
            if (actual.isPresent()) {
                Assert.fail();
            }
        } else {
            if (actual.isPresent()) {
                Assert.assertEquals(expected, actual.get());
            } else {
                Assert.fail();
            }
        }
    }

    public void findIntersection(LineSegment2D ls0, LineSegment2D ls1, Point2D expected) {
        Optional<Point2D> actual = ls0.findIntersection(ls1);

        if (expected == null) {
            if (actual.isPresent()) {
                Assert.fail();
            }
        } else {
            if (actual.isPresent()) {
                Assert.assertEquals(expected, actual.get());
            } else {
                Assert.fail();
            }
        }

    }

    @Test
    public void testFindIntersectionBetweenRayAndLineSegment() {
        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(13.3, 0)),
                new Ray2D(new Point2D(1, 1), new Vector2D(0, -1)),
                new Point2D(1, 0));


        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0)),
                new Ray2D(new Point2D(1, 1), new Vector2D(0, 1)),
                null);

        findIntersection(
                new LineSegment2D(new Point2D(1, 1), new Point2D(-1, -1)),
                new Ray2D(new Point2D(-1, 1), new Vector2D(1, -1)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)),
                new Ray2D(new Point2D(1, -1), new Vector2D(-1, 1)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(0, -100), new Point2D(0, 100)),
                new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(100, 0), new Point2D(-100, 0)),
                new Ray2D(new Point2D(0, -10), new Vector2D(0, 1)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000)),
                new Ray2D(new Point2D(100, -1), new Vector2D(1, -1)),
                null);

        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(1, 0)),
                new Ray2D(new Point2D(2, 0), new Vector2D(1, 1)),
                null);
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
        findIntersection(
                new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0)),
                new LineSegment2D(new Point2D(0, 1), new Point2D(0, -1)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0)),
                new LineSegment2D(new Point2D(1, 0), new Point2D(2, 0)),
                new Point2D(1, 0));

        findIntersection(
                new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0)),
                new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)),
                new Point2D(0, 0));

        findIntersection(
                new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0)),
                new LineSegment2D(new Point2D(4, 0), new Point2D(2, 0)),
                null);

        findIntersection(
                new LineSegment2D(new Point2D(-1, 0), new Point2D(1, 0)),
                new LineSegment2D(new Point2D(2, 0), new Point2D(4, 0)),
                null);

        findIntersection(
                new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)),
                new LineSegment2D(new Point2D(1, 0), new Point2D(1, 10)),
                new Point2D(1, 1));

        findIntersection(
                new LineSegment2D(new Point2D(-1, 1), new Point2D(0, 1)),
                new LineSegment2D(new Point2D(0, 2), new Point2D(1, 2)),
                null);

        findIntersection(
                new LineSegment2D(new Point2D(-1, 1), new Point2D(1, 1)),
                new LineSegment2D(new Point2D(1, 0), new Point2D(-1, 2)),
                new Point2D(0, 1));

    }
}
