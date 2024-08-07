package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class LineSegment2DTest {

    @Test
    public void testFindIntersectionCase1() {
        LineSegment2D ls = new LineSegment2D(new Point2D(0, 0), new Point2D(13.3f, 0));
        Ray2D ray = new Ray2D(new Point2D(1, 1), new Vector2D(0, -1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
        Assert.assertEquals(intersection.get(), new Point2D(1, 0));
    }

    @Test
    public void testFindIntersectionCase2() {
        LineSegment2D ls = new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0));
        Ray2D ray = new Ray2D(new Point2D(1, 1), new Vector2D(0, 1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isEmpty());
    }

    @Test
    public void testFindIntersectionCase3() {
        LineSegment2D ls = new LineSegment2D(new Point2D(1, 1), new Point2D(-1, -1));
        Ray2D ray = new Ray2D(new Point2D(-1, 1), new Vector2D(1, -1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
        Assert.assertEquals(intersection.get(), new Point2D(0, 0));
    }

    @Test
    public void testFindIntersectionCase4() {
        LineSegment2D ls = new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1));
        Ray2D ray = new Ray2D(new Point2D(1, -1), new Vector2D(-1, 1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
        Assert.assertEquals(intersection.get(), new Point2D(0, 0));
    }

    @Test
    public void testFindIntersectionCase5() {
        LineSegment2D ls = new LineSegment2D(new Point2D(0, -100), new Point2D(0, 100));
        Ray2D ray = new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
        Assert.assertEquals(intersection.get(), new Point2D(0, 0));
    }

    @Test
    public void testFindIntersectionCase6() {
        LineSegment2D ls = new LineSegment2D(new Point2D(100, 0), new Point2D(-100, 0));
        Ray2D ray = new Ray2D(new Point2D(0, -10), new Vector2D(0, 1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
        Assert.assertEquals(intersection.get(), new Point2D(0, 0));
    }

    @Test
    public void testFindIntersectionCase7() {
        LineSegment2D ls = new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000));
        Ray2D ray = new Ray2D(new Point2D(100, -1), new Vector2D(1, -1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isEmpty());
    }

    @Test
    public void testFindIntersectionCase8() {
        LineSegment2D ls = new LineSegment2D(new Point2D(-1000, -1000), new Point2D(1000, 1000));
        Ray2D ray = new Ray2D(new Point2D(100, 0), new Vector2D(0, 1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isPresent());
    }

    @Test
    public void testFindIntersectionCase9() {
        LineSegment2D ls = new LineSegment2D(new Point2D(0, 0), new Point2D(1, 0));
        Ray2D ray = new Ray2D(new Point2D(2, 0), new Vector2D(1, 1));
        Optional<Point2D> intersection = ls.findIntersection(ray);
        Assert.assertTrue(intersection.isEmpty());
    }
}
