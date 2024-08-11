package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CircleTest {

    @Test
    public void testCalculatePointAt() {
        calculatePointAt(new Circle(new Point2D(0, 0), 1), 0,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 10), 0,
                (Point2D point) -> Assert.assertEquals(new Point2D(10, 0), point));

        calculatePointAt(new Circle(new Point2D(10, 0), 10), 0,
                (Point2D point) -> Assert.assertEquals(new Point2D(20, 0), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 1), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(-1, 0), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 1), 2 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 1), 0.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 1), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 1), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 1), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 2), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -2), point));

        calculatePointAt(new Circle(new Point2D(0, 2), 2), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 0), point));

        calculatePointAt(new Circle(new Point2D(2, 2), 2), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 2), point));

        calculatePointAt(new Circle(new Point2D(2, 2), 2), 0.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(2, 4), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 2), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -2), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 2), (30.0 / 180.0) * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(Math.sqrt(3), 1), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 2), (60.0 / 180.0) * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, Math.sqrt(3)), point));

        calculatePointAt(new Circle(new Point2D(0, 0), 2), -(60.0 / 180.0) * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, -Math.sqrt(3)), point));
    }

    @Test
    public void calculateGradientAt() {
        calculateGradientAt(new Circle(new Point2D(0, 0), 1), 0,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 1), point));

        calculateGradientAt(new Circle(new Point2D(0, 0), 1), 0.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(-1, 0), point));

        calculateGradientAt(new Circle(new Point2D(0, 0), 1), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));

        calculateGradientAt(new Circle(new Point2D(0, 0), 1), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculateGradientAt(new Circle(new Point2D(0, 0), 1), 2 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 1), point));

        calculateGradientAt(new Circle(new Point2D(100, 200), 1), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));

    }

    @Test
    public void calculateNormalAt() {
        calculateNormalAt(new Circle(new Point2D(0, 0), 1), 0,
                (Point2D point) -> Assert.assertEquals(new Point2D(-1, 0), point));

        calculateNormalAt(new Circle(new Point2D(0, 0), 1), 0.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, -1), point));

        calculateNormalAt(new Circle(new Point2D(0, 0), 1), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculateNormalAt(new Circle(new Point2D(0, 0), 1), 1.5 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(0, 1), point));

        calculateNormalAt(new Circle(new Point2D(0, 0), 1), 2 * Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(-1, 0), point));

        calculateNormalAt(new Circle(new Point2D(100, 100), 1), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculateNormalAt(new Circle(new Point2D(0, 0), 10), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

        calculateNormalAt(new Circle(new Point2D(100, 100), 10), Math.PI,
                (Point2D point) -> Assert.assertEquals(new Point2D(1, 0), point));

    }

    @Test
    public void calculateSlopeOfTangent() {
        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 10), 0,
                (Double slope) -> Assert.assertTrue(Double.isNaN(slope)));

        calculateSlopeOfTangent(new Circle(new Point2D(1, 1), 1), 0,
                (Double slope) -> Assert.assertTrue(Double.isNaN(slope)));

        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 1), 0,
                (Double slope) -> Assert.assertTrue(Double.isNaN(slope)));

        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 1), 0.5 * Math.PI,
                (Double slope) -> Assert.assertEquals(0.0, slope, Util.EPSILON));

        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 1), Math.PI,
                (Double slope) -> Assert.assertTrue(Double.isNaN(slope)));

        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 1), 1.5 * Math.PI,
                (Double slope) -> Assert.assertEquals(0.0, slope, Util.EPSILON));

        calculateSlopeOfTangent(new Circle(new Point2D(0, 0), 1), 2 * Math.PI,
                (Double slope) -> Assert.assertTrue(Double.isNaN(slope)));
    }

    @Test
    public void findParametersAtWhichTangentLinesHasGivenSlope() {

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(0, 0), 1), Double.NaN,
                List.of(0.0, Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(0, 0), 2), Double.NaN,
                List.of(0.0, Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(1, 1), 1), Double.NaN,
                List.of(0.0, Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(1, 1), 2), Double.NaN,
                List.of(0.0, Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(0, 0), 1), 0.0,
                List.of(0.5 * Math.PI, 1.5 * Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(0, 0), 1), 1.0,
                List.of(-(45.0 / 180.0) * Math.PI, (135.0 / 180.0) * Math.PI));

        findParametersAtWhichTangentLinesHasGivenSlope(new Circle(new Point2D(0, 0), 1), -1.0,
                List.of((45.0 / 180.0) * Math.PI, (225.0 / 180.0) * Math.PI));
    }

    @Test
    public void testDoesIntersectWithLine() {
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(-10, -10), new Point2D(10, 10)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 10), new Line2D(new Point2D(-10, -10), new Point2D(10, 10)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(1, 1), 10), new Line2D(new Point2D(-10, -10), new Point2D(10, 10)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(10, -10), new Point2D(10, 10)), Assert::assertFalse);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(10, 10), new Point2D(-10, 10)), Assert::assertFalse);
        doesIntersect(new Circle(new Point2D(0, 0), 10), new Line2D(new Point2D(2, 2), new Point2D(3, 3)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(0, -0.5), new Point2D(0.5, 0)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(0, -1), new Point2D(0, 1)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new Line2D(new Point2D(0, -1), new Point2D(1, -1)), Assert::assertTrue);
    }

    @Test
    public void testDoesIntersectWithLineSegment() {
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(-10, -10), new Point2D(10, 10)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(0.0, 0.0), new Point2D(0.5, 0.5)), Assert::assertFalse);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 10), new LineSegment2D(new Point2D(-10, -10), new Point2D(10, 10)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(1, 0), new Point2D(10, 0)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(1.0001, 0), new Point2D(10, 0)), Assert::assertFalse);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(-0.999, 0), new Point2D(0.999, 0)), Assert::assertFalse);
        doesIntersect(new Circle(new Point2D(0, 0), 1), new LineSegment2D(new Point2D(-1.0, 0), new Point2D(0.999, 0)), Assert::assertTrue);
        doesIntersect(new Circle(new Point2D(0, 0), 2), new LineSegment2D(new Point2D(-1.0, 0), new Point2D(0.999, 0)), Assert::assertFalse);
    }

    @Test
    public void testDoesIntersectWithRay() {
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-10, -10), new Vector2D(1, 1)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(10, 10), new Vector2D(-1, -1)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(10, 0), new Vector2D(-1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-10, 0), new Vector2D(1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(0, 10), new Vector2D(0, -1)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 1), new Vector2D(1, 1)), Assert::assertFalse);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(0.5, 0.5), new Vector2D(1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-1, 1), new Vector2D(1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-1, -1), new Vector2D(1, 1)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.5, 0.5), new Vector2D(1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.5, 0.5), new Vector2D(-1, 0)), Assert::assertTrue);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.5, 1), new Vector2D(-1, 0)), Assert::assertFalse);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-1.5, 1), new Vector2D(-1, 0)), Assert::assertFalse);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(2, 2), new Vector2D(-1, 0)), Assert::assertFalse);
        doesIntersect(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.5, 0), new Vector2D(-1, 0)), Assert::assertTrue);
    }

    @Test
    public void testFindClosestPointToLine() {
        Assert.assertEquals(new Point2D(1, 0), Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(1, -1), new Point2D(1, 1))));
        Assert.assertEquals(new Point2D(-1, 0), Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(-1, -1), new Point2D(-1, 1))));
        Assert.assertEquals(new Point2D(1, 0), Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(10, -1), new Point2D(10, 1))));
        Assert.assertEquals(new Point2D(0, -1), Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(-1, -10), new Point2D(1, -10))));
        Assert.assertEquals(new Point2D(-0.5 * Math.sqrt(2), -0.5 * Math.sqrt(2)),
                Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(-1, 0), new Point2D(0, -1))));

        Assert.assertEquals(new Point2D(0.5 * Math.sqrt(2), 0.5 * Math.sqrt(2)),
                Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(10, 0), new Point2D(0, 10))));

        Assert.assertEquals(new Point2D(0, 1), Circle.UNIT_CIRCLE.findClosestPointToLine(new Line2D(new Point2D(1, 1), new Point2D(-1, 1))));
    }

    @Test
    public void testFindLineIntersection() {
        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(10, -10), new Point2D(10, 10)),
                Set.of());

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(0, 0), new Point2D(10, 0)),
                Set.of(new Point2D(-1, 0), new Point2D(1, 0)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(-10, 0), new Point2D(10, 0)),
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(1, -1), new Point2D(1, 1)),
                Set.of(new Point2D(1, 0)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(-1, -1), new Point2D(1, 1)),
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(0, -0.5), new Point2D(0, 0.5)),
                Set.of(new Point2D(0, -1), new Point2D(0, 1)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(-0.5, 0), new Point2D(0.5, 0)),
                Set.of(new Point2D(-1, 0), new Point2D(1, 0)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(1, 0), new Point2D(0, 1)),
                Set.of(new Point2D(1, 0), new Point2D(0, 1)));

        testFindLineIntersection(Circle.UNIT_CIRCLE, new Line2D(new Point2D(10, 0), new Point2D(0, 10)),
                Set.of());

    }

    @Test
    public void testFindLineSegmentIntersection() {
        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(10, -10), new Point2D(10, 10)),
                Set.of());

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0)),
                Set.of(new Point2D(1, 0)));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(-10, 0), new Point2D(10, 0)),
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(1, -1), new Point2D(1, 1)),
                Set.of(new Point2D(1, 0)));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1)),
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(0, -0.5), new Point2D(0, 0.5)),
                Set.of());

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(-0.5, 0), new Point2D(0.5, 0)),
                Set.of());

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(1, 0), new Point2D(0, 1)),
                Set.of(new Point2D(1, 0), new Point2D(0, 1)));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(10, 0), new Point2D(0, 10)),
                Set.of());

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(0, 0), new Point2D(10, 10)),
                Set.of(new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(0, 0.999), new Point2D(0, 1.111)),
                Set.of(new Point2D(0, 1)));

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(0.999, 0), new Point2D(-0.999, 0)),
                Set.of());

        testFindLineSegmentIntersection(Circle.UNIT_CIRCLE, new LineSegment2D(new Point2D(1.999, 0), new Point2D(-1.999, 0)),
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)));

    }

    @Test
    public void testFindRayIntersection() {
        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 1), new Vector2D(1, 1)),
                Set.of());

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1)),
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0)),
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 0), new Vector2D(1, 0)),
                Set.of(new Point2D(1, 0)));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 0), new Vector2D(0, 1)),
                Set.of(new Point2D(1, 0)));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(1, 0), new Vector2D(0, 1)),
                Set.of(new Point2D(1, 0)));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.1, -0.1), new Vector2D(1, 1)),
                Set.of(new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))));

        testFindRayIntersection(Circle.UNIT_CIRCLE, new Ray2D(new Point2D(-0.5, 0.5), new Vector2D(1, 1)),
                Set.of(new Point2D(0, 1)));
    }

    public void calculatePointAt(Circle circle, double theta, Consumer<Point2D> action) {
        action.accept(circle.calculatePointAt(theta));
    }

    public void calculateGradientAt(Circle circle, double theta, Consumer<Point2D> action) {
        action.accept(circle.calculateGradientAt(theta));
    }

    public void calculateNormalAt(Circle circle, double theta, Consumer<Point2D> action) {
        action.accept(circle.calculateNormalAt(theta));
    }

    public void calculateSlopeOfTangent(Circle circle, double theta, Consumer<Double> action) {
        action.accept(circle.calculateSlopeOfTangent(theta));
    }

    public void findParametersAtWhichTangentLinesHasGivenSlope(Circle circle, double slope, List<Double> expected) {
        var list = circle.findParametersAtWhichTangentLinesHasGivenSlope(slope);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(expected, list);
    }

    public void doesIntersect(Circle circle, Line2D line, Consumer<Boolean> action) {
        action.accept(circle.doesIntersect(line));
    }

    public void doesIntersect(Circle circle, LineSegment2D ls, Consumer<Boolean> action) {
        action.accept(circle.doesIntersect(ls));
    }

    public void doesIntersect(Circle circle, Ray2D ray, Consumer<Boolean> action) {
        action.accept(circle.doesIntersect(ray));
    }

    public void testFindLineIntersection(Circle circle, Line2D line, Set<Point2D> expected) {
        List<Point2D> actual = circle.findIntersection(line);
        Assert.assertEquals(expected, Set.copyOf(actual));

    }

    public void testFindLineSegmentIntersection(Circle circle, LineSegment2D lineSegment, Set<Point2D> expected) {
        List<Point2D> actual = circle.findIntersection(lineSegment);
        Assert.assertEquals(expected, Set.copyOf(actual));

    }

    public void testFindRayIntersection(Circle circle, Ray2D ray, Set<Point2D> expected) {
        List<Point2D> actual = circle.findIntersection(ray);
        Assert.assertEquals(expected, Set.copyOf(actual));
    }

}
