package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class Ray2DTest {

    public Optional<Double> findIntersectionParameter(Ray2D ray0, Ray2D ray1) {
        return ray0.findParameterIfIntersects(ray1);
    }

    public Optional<Double> findParameterForGivenPoint(Ray2D ray, Point2D point) {
        return ray.findParameterForGivenPoint(point);
    }

    public Optional<Boolean> isCollinear(Ray2D ray0, Ray2D ray1) {
        return Optional.of(ray0.isCollinear(ray1));
    }

    public Optional<Boolean> isPointOnRay(Ray2D ray, Point2D point) {
        return Optional.of(ray.isPointOnRay(point));
    }

    public Optional<Point2D> findIntersection(Ray2D ray0, Ray2D ray1) {
        return ray0.findIntersection(ray1);
    }

    public Optional<Point2D> findIntersection(Ray2D ray, Line2D line) {
        return ray.findIntersection(line);
    }

    @Test
    public void testPointCalculatePointAt() {
        Ray2D ray = new Ray2D(new Point2D(1.0, 1.0), new Vector2D(1.0, 0.0));
        Point2D actual = ray.calculatePointAt(1.123);
        Point2D expected = new Point2D(2.123, 1.0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindIntersectionBetweenRayAndLine() {
        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Line2D(new Point2D(0, 0), new Point2D(-1, -1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new Ray2D(new Point2D(0, 1), new Vector2D(0, 1)),
                new Line2D(new Point2D(0, 0), new Point2D(-1, -1))
        ).ifPresent((Point2D point) -> Assert.fail());

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Line2D(new Point2D(1, 1), new Point2D(1, 2))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 1), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Line2D(new Point2D(0.5, 0.5), new Point2D(-1, 0.5))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0.5, 0.5), point));

        findIntersection(
                new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0)),
                new Line2D(new Point2D(-1, -1), new Point2D(1, 1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new Ray2D(new Point2D(1, 1), new Vector2D(0, -1)),
                new Line2D(new Point2D(-1, 0), new Point2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Line2D(new Point2D(0, -1), new Point2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.fail());

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)),
                new Line2D(new Point2D(0, -1), new Point2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(-0.5, -0.5), point));

        findIntersection(
                new Ray2D(new Point2D(-100, -100), new Vector2D(1, 1)),
                new Line2D(new Point2D(0, -1), new Point2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(-0.5, -0.5), point));

        findIntersection(
                new Ray2D(new Point2D(0, -1), new Vector2D(0, 1)),
                new Line2D(new Point2D(0, -1), new Point2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));
    }

    @Test
    public void testFindIntersectionBetweenTwoRays() {
        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(0, 1), new Vector2D(1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 1), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(1, 1), new Vector2D(-1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 1), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 0)),
                new Ray2D(new Point2D(1, 1), new Vector2D(0, -1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(0, 1)),
                new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(0, 1)),
                new Ray2D(new Point2D(1, 1), new Vector2D(-1, 0))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0, 1), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(0.5, 0.5), new Vector2D(-1, 1))
        ).ifPresent((Point2D point) -> Assert.assertEquals(new Point2D(0.5, 0.5), point));

        findIntersection(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(-100, 0), new Vector2D(-1, -1))
        ).ifPresent((Point2D point) -> Assert.fail());

        findIntersection(
                new Ray2D(new Point2D(1, 1), new Vector2D(1, 0)),
                new Ray2D(new Point2D(-1, -1), new Vector2D(-1, 0))
        ).ifPresent((Point2D point) -> Assert.fail());
    }

    @Test
    public void testFindParameterIfIntersects() {
        findIntersectionParameter(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(0, 1), new Vector2D(1, 0))
        ).ifPresent((Double parameter) -> Assert.assertEquals(Math.sqrt(2), parameter, Util.EPSILON));

        findIntersectionParameter(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, 0)),
                new Ray2D(new Point2D(-5, 5), new Vector2D(0, -1))
        ).ifPresent((Double parameter) -> Assert.assertEquals(5.0, parameter, Util.EPSILON));

        findIntersectionParameter(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Ray2D(new Point2D(1, 0), new Vector2D(-1, -1))
        ).ifPresent((Double parameter) -> Assert.fail());

        findIntersectionParameter(
                new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0)),
                new Ray2D(new Point2D(0, 0), new Vector2D(0, -1))
        ).ifPresent((Double parameter) -> Assert.assertEquals(10.0, parameter, Util.EPSILON));


        findIntersectionParameter(
                new Ray2D(new Point2D(0, 0), new Vector2D(0, -1)),
                new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0))
        ).ifPresent((Double parameter) -> Assert.assertEquals(0.0, parameter, Util.EPSILON));
    }

    @Test
    public void testFindParameter() {
        findParameterForGivenPoint(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 0)),
                new Point2D(0, 1)
        ).ifPresent((Double parameter) -> Assert.fail());

        findParameterForGivenPoint(
                new Ray2D(new Point2D(1, 1), new Vector2D(1, 0)),
                new Point2D(0, 2)
        ).ifPresent((Double parameter) -> Assert.fail());

        findParameterForGivenPoint(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 0)),
                new Point2D(100, 0)
        ).ifPresent((Double parameter) -> Assert.assertEquals(100, parameter, Util.EPSILON));

        findParameterForGivenPoint(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(100, 100)
        ).ifPresent((Double parameter) -> Assert.assertEquals(100 * Math.sqrt(2), parameter, Util.EPSILON));

        findParameterForGivenPoint(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, 0)),
                new Point2D(100, 0)
        ).ifPresent((Double parameter) -> Assert.assertEquals(-100.0, parameter, Util.EPSILON));

        findParameterForGivenPoint(
                new Ray2D(new Point2D(1, 1), new Vector2D(1, 1)),
                new Point2D(100, 100)
        ).ifPresent((Double parameter) -> Assert.assertEquals(99f * Math.sqrt(2.0), parameter, Util.EPSILON));
    }

    @Test
    public void testIsCollinear() {
        isCollinear(
                new Ray2D(new Point2D(1, 1), new Vector2D(1, 1)),
                new Ray2D(new Point2D(-1, -1), new Vector2D(-1, -1))
        ).ifPresent(Assert::assertTrue);

        isCollinear(
                new Ray2D(new Point2D(1, 0), new Vector2D(1, 0)),
                new Ray2D(new Point2D(-1, 0), new Vector2D(-1, 0))
        ).ifPresent(Assert::assertTrue);

        isCollinear(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)),
                new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1))
        ).ifPresent(Assert::assertTrue);

        isCollinear(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)),
                new Ray2D(new Point2D(1, 1), new Vector2D(1, 1))
        ).ifPresent(Assert::assertTrue);

        isCollinear(
                new Ray2D(new Point2D(0, 0), new Vector2D(-1, -1)),
                new Ray2D(new Point2D(1, 0), new Vector2D(-1, -1))
        ).ifPresent(Assert::assertFalse);

        isCollinear(
                new Ray2D(new Point2D(0, 0), new Vector2D(0, -1)),
                new Ray2D(new Point2D(1, 0), new Vector2D(0, -1))
        ).ifPresent(Assert::assertFalse);

        isCollinear(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 2)),
                new Ray2D(new Point2D(1, 0), new Vector2D(2, -1))
        ).ifPresent(Assert::assertFalse);
    }

    @Test
    public void testIsPointOnRay() {
        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(0.5, 0.5)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(0.0, 0.0)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(1, 1)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(200, 200)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(1.00001, 1)
        ).ifPresent(Assert::assertFalse);

        isPointOnRay(
                new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1)),
                new Point2D(1.01, 1.01)
        ).ifPresent(Assert::assertFalse);

        isPointOnRay(
                new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1)),
                new Point2D(0, 0)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 0)),
                new Point2D(100, 0)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(0, 1)),
                new Point2D(0, 100)
        ).ifPresent(Assert::assertTrue);

        isPointOnRay(
                new Ray2D(new Point2D(0, 0), new Vector2D(1, 1)),
                new Point2D(0, 0.1)
        ).ifPresent(Assert::assertFalse);
    }

    @Test
    public void testFindClosestPointToCircleCenter() {
        Assert.assertEquals(new Point2D(0,0), new Ray2D(new Point2D(0,0), new Vector2D(1,1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0.5,0.5), new Ray2D(new Point2D(0.5,0.5), new Vector2D(1,1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(0,0), new Ray2D(new Point2D(0.5,0.5), new Vector2D(-1,-1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(1,0), new Ray2D(new Point2D(1,-1), new Vector2D(0,1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(1,-1), new Ray2D(new Point2D(1,-1), new Vector2D(0,-1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(5,5),  new Ray2D(new Point2D(10,0), new Vector2D(-1,1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
        Assert.assertEquals(new Point2D(10,0),  new Ray2D(new Point2D(10,-10), new Vector2D(0,1)).findClosestPointToCircleCenter(Circle.UNIT_CIRCLE));
    }
}
