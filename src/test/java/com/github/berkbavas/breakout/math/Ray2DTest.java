package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class Ray2DTest {

    @Test
    public void testPointPointAt() {
        Ray2D ray = new Ray2D(new Point2D(1.0f, 1.0f), new Vector2D(1.0f, 0.0f));
        Point2D actual = ray.pointAt(1.123f);
        Point2D expected = new Point2D(2.123f, 1.0f);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findIntersectionCase1() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(1.0f, 1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(0.0f, 1.0f);
        Vector2D direction2 = new Vector2D(1.0f, 0.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Optional<Matrix2x1> result = ray1.findIntersection(ray2);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getM00(), (float) Math.sqrt(2.0f), Util.EPSILON);
    }

    @Test
    public void findIntersectionCase2() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(-1.0f, 0.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(-5.0f, 5.0f);
        Vector2D direction2 = new Vector2D(0.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Optional<Matrix2x1> result = ray1.findIntersection(ray2);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getM00(), 5.0f, Util.EPSILON);
        Assert.assertEquals(result.get().getM10(), 5.0f, Util.EPSILON);
    }

    @Test
    public void findIntersectionCase3() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(1.0f, 1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 0.0f);
        Vector2D direction2 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Optional<Matrix2x1> result = ray1.findIntersection(ray2);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findIntersectionCase4() {
        Point2D origin1 = new Point2D(10.0f, 0.0f);
        Vector2D direction1 = new Vector2D(-1.0f, 0.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(0.0f, 0.0f);
        Vector2D direction2 = new Vector2D(0.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Optional<Matrix2x1> result = ray1.findIntersection(ray2);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getM00(), 10.0f, Util.EPSILON);
        Assert.assertEquals(result.get().getM10(), 0.0f, Util.EPSILON);
    }

    @Test
    public void testFindParameterCase1() {
        Ray2D ray = new Ray2D(new Point2D(0, 0), new Vector2D(1.0f, 0));
        Optional<Float> result = ray.findParameterFor(new Point2D(0.0f, 1.0f));
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFindParameterCase2() {
        Point2D origin = new Point2D(1.0f, 1.0f);
        Vector2D direction = new Vector2D(1.0f, 0.0f);
        Ray2D ray = new Ray2D(origin, direction);

        Optional<Float> result = ray.findParameterFor(new Point2D(0.0f, 2.0f));
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFindParameterCase3() {
        Point2D origin = new Point2D(0.0f, 0.0f);
        Vector2D direction = new Vector2D(1.0f, 0.0f);
        Ray2D ray = new Ray2D(origin, direction);

        Optional<Float> result = ray.findParameterFor(new Point2D(100.0f, 0.0f));
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), 100.0f, Util.EPSILON);
    }


    @Test
    public void testFindParameterCase4() {
        Point2D origin = new Point2D(0.0f, 0.0f);
        Vector2D direction = new Vector2D(1.0f, 1.0f);
        Ray2D ray = new Ray2D(origin, direction);

        Optional<Float> result = ray.findParameterFor(new Point2D(100.0f, 100.0f));
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), 100f * (float) Math.sqrt(2.0f), Util.EPSILON);
    }

    @Test
    public void testFindParameterCase5() {
        Point2D origin = new Point2D(0.0f, 0.0f);
        Vector2D direction = new Vector2D(-1.0f, 0.0f);
        Ray2D ray = new Ray2D(origin, direction);

        Optional<Float> result = ray.findParameterFor(new Point2D(100.0f, 0.0f));
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), -100f, Util.EPSILON);
    }

    @Test
    public void testFindParameterCase6() {
        Point2D origin = new Point2D(1.0f, 1.0f);
        Vector2D direction = new Vector2D(1.0f, 1.0f);
        Ray2D ray = new Ray2D(origin, direction);

        Optional<Float> result = ray.findParameterFor(new Point2D(100.0f, 100.0f));
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), 99f * (float) Math.sqrt(2.0f), Util.EPSILON);
    }

    @Test
    public void testIsCollinearCase1() {
        Point2D origin1 = new Point2D(1.0f, 1.0f);
        Vector2D direction1 = new Vector2D(1.0f, 1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);


        Point2D origin2 = new Point2D(-1.0f, -1.0f);
        Vector2D direction2 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertTrue(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase2() {
        Point2D origin1 = new Point2D(1.0f, 0.0f);
        Vector2D direction1 = new Vector2D(1.0f, 0.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(-1.0f, 0.0f);
        Vector2D direction2 = new Vector2D(-1.0f, 0.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertTrue(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase3() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 1.0f);
        Vector2D direction2 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertTrue(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase4() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 1.0f);
        Vector2D direction2 = new Vector2D(1.0f, 1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertTrue(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase5() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 0.0f);
        Vector2D direction2 = new Vector2D(-1.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertFalse(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase6() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(0.0f, -1.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 0.0f);
        Vector2D direction2 = new Vector2D(0.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertFalse(ray1.isCollinear(ray2));
    }

    @Test
    public void testIsCollinearCase7() {
        Point2D origin1 = new Point2D(0.0f, 0.0f);
        Vector2D direction1 = new Vector2D(1.0f, 2.0f);
        Ray2D ray1 = new Ray2D(origin1, direction1);

        Point2D origin2 = new Point2D(1.0f, 0.0f);
        Vector2D direction2 = new Vector2D(2.0f, -1.0f);
        Ray2D ray2 = new Ray2D(origin2, direction2);

        Assert.assertFalse(ray1.isCollinear(ray2));
    }
}
