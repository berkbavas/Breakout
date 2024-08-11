package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class LineSegment2DTest {

    public Optional<Point2D> findIntersection(LineSegment2D ls, Ray2D ray) {
        return ls.findIntersection(ray);
    }

    @Test
    public void testFindIntersectionBetweenRayAndLineSegment() {
        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(13.3, 0)),
                new Ray2D(new Point2D(1, 1), new Vector2D(0, -1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0)),
                new Ray2D(new Point2D(1, 1), new Vector2D(0, 1))
        ).ifPresent((Point2D) -> Assert.fail());

        findIntersection(
                new LineSegment2D(new Point2D(1, 1), new Point2D(-1, -1)),
                new Ray2D(new Point2D(-1, 1), new Vector2D(1, -1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)),
                new Ray2D(new Point2D(1, -1), new Vector2D(-1, 1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new LineSegment2D(new Point2D(0, -100), new Point2D(0, 100)),
                new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new LineSegment2D(new Point2D(100, 0), new Point2D(-100, 0)),
                new Ray2D(new Point2D(0, -10), new Vector2D(0, 1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000)),
                new Ray2D(new Point2D(100, -1), new Vector2D(1, -1))
        ).ifPresent((Point2D point) -> Assert.fail());

        findIntersection(
                new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000)),
                new Ray2D(new Point2D(100, 0), new Vector2D(0, 1))
        ).ifPresent((Point2D point) -> Assert.assertTrue(true));

        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(1, 0)),
                new Ray2D(new Point2D(2, 0), new Vector2D(1, 1))
        ).ifPresent((Point2D point) -> Assert.assertTrue(true));

        findIntersection(
                new LineSegment2D(new Point2D(0, 0), new Point2D(1, 0)),
                new Ray2D(new Point2D(2, 0), new Vector2D(1, 1))
        ).ifPresent((Point2D point) -> Assert.fail());
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
}
