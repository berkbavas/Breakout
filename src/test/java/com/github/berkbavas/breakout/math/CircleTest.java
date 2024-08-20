package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CircleTest {

    @Test
    public void testCalculatePointAt() {
        Assert.assertEquals(new Point2D(1, 0), new Circle(new Point2D(0, 0), 1).calculatePointAt(0));
        Assert.assertEquals(new Point2D(10, 0), new Circle(new Point2D(0, 0), 10).calculatePointAt(0));
        Assert.assertEquals(new Point2D(20, 0), new Circle(new Point2D(10, 0), 10).calculatePointAt(0));
        Assert.assertEquals(new Point2D(-1, 0), new Circle(new Point2D(0, 0), 1).calculatePointAt(Math.PI));
        Assert.assertEquals(new Point2D(1, 0), new Circle(new Point2D(0, 0), 1).calculatePointAt(2 * Math.PI));
        Assert.assertEquals(new Point2D(0, 1), new Circle(new Point2D(0, 0), 1).calculatePointAt(0.5 * Math.PI));
        Assert.assertEquals(new Point2D(0, -1), new Circle(new Point2D(0, 0), 1).calculatePointAt(1.5 * Math.PI));
        Assert.assertEquals(new Point2D(0, -2), new Circle(new Point2D(0, 0), 2).calculatePointAt(1.5 * Math.PI));
        Assert.assertEquals(new Point2D(0, 0), new Circle(new Point2D(0, 2), 2).calculatePointAt(1.5 * Math.PI));
        Assert.assertEquals(new Point2D(0, 2), new Circle(new Point2D(2, 2), 2).calculatePointAt(Math.PI));
        Assert.assertEquals(new Point2D(2, 4), new Circle(new Point2D(2, 2), 2).calculatePointAt(0.5 * Math.PI));
        Assert.assertEquals(new Point2D(0, -2), new Circle(new Point2D(0, 0), 2).calculatePointAt(1.5 * Math.PI));
        Assert.assertEquals(new Point2D(Math.sqrt(3), 1), new Circle(new Point2D(0, 0), 2).calculatePointAt((30.0 / 180.0) * Math.PI));
        Assert.assertEquals(new Point2D(1, Math.sqrt(3)), new Circle(new Point2D(0, 0), 2).calculatePointAt((60.0 / 180.0) * Math.PI));
        Assert.assertEquals(new Point2D(1, -Math.sqrt(3)), new Circle(new Point2D(0, 0), 2).calculatePointAt(-(60.0 / 180.0) * Math.PI));
    }

    @Test
    public void calculateGradientAt() {
        Assert.assertEquals(new Vector2D(0, 1), Circle.UNIT_CIRCLE.calculateGradientAt(0));
        Assert.assertEquals(new Vector2D(-1, 0), Circle.UNIT_CIRCLE.calculateGradientAt(0.5 * Math.PI));
        Assert.assertEquals(new Vector2D(0, -1), Circle.UNIT_CIRCLE.calculateGradientAt(Math.PI));
        Assert.assertEquals(new Vector2D(1, 0), Circle.UNIT_CIRCLE.calculateGradientAt(1.5 * Math.PI));
        Assert.assertEquals(new Vector2D(0, 1), Circle.UNIT_CIRCLE.calculateGradientAt(2 * Math.PI));
        Assert.assertEquals(new Vector2D(0, -1), new Circle(new Point2D(100, 200), 1).calculateGradientAt(Math.PI));
    }

    @Test
    public void calculateNormalAt() {
        Assert.assertEquals(new Vector2D(-1, 0), Circle.UNIT_CIRCLE.calculateNormalAt(0));
        Assert.assertEquals(new Vector2D(0, -1), Circle.UNIT_CIRCLE.calculateNormalAt(0.5 * Math.PI));
        Assert.assertEquals(new Vector2D(1, 0), Circle.UNIT_CIRCLE.calculateNormalAt(Math.PI));
        Assert.assertEquals(new Vector2D(0, 1), Circle.UNIT_CIRCLE.calculateNormalAt(1.5 * Math.PI));
        Assert.assertEquals(new Vector2D(-1, 0), Circle.UNIT_CIRCLE.calculateNormalAt(2 * Math.PI));
        Assert.assertEquals(new Vector2D(1, 0), new Circle(new Point2D(100, 100), 1).calculateNormalAt(Math.PI));
        Assert.assertEquals(new Vector2D(1, 0), new Circle(new Point2D(0, 0), 10).calculateNormalAt(Math.PI));
        Assert.assertEquals(new Vector2D(1, 0), new Circle(new Point2D(100, 100), 10).calculateNormalAt(Math.PI));
    }

    @Test
    public void calculateSlopeOfTangent() {
        Assert.assertTrue(Double.isNaN(new Circle(new Point2D(0, 0), 1).calculateSlopeOfTangent(0)));
        Assert.assertTrue(Double.isNaN(new Circle(new Point2D(1, 1), 1).calculateSlopeOfTangent(0)));
        Assert.assertTrue(Double.isNaN(new Circle(new Point2D(0, 0), 10).calculateSlopeOfTangent(0)));
        Assert.assertTrue(Double.isNaN(new Circle(new Point2D(0, 0), 10).calculateSlopeOfTangent(2 * Math.PI)));
        Assert.assertTrue(Double.isNaN(new Circle(new Point2D(0, 0), 1).calculateSlopeOfTangent(Math.PI)));

        Assert.assertEquals(0.0, Circle.UNIT_CIRCLE.calculateSlopeOfTangent(0.5 * Math.PI), Util.EPSILON);
        Assert.assertEquals(0.0, Circle.UNIT_CIRCLE.calculateSlopeOfTangent(1.5 * Math.PI), Util.EPSILON);
    }

    @Test
    public void findParametersForGivenSlope() {
        Assert.assertEquals(List.of(0.0, Math.PI), new Circle(new Point2D(0, 0), 1).findParametersForGivenSlope(Double.NaN));
        Assert.assertEquals(List.of(0.0, Math.PI), new Circle(new Point2D(0, 0), 2).findParametersForGivenSlope(Double.NaN));
        Assert.assertEquals(List.of(0.0, Math.PI), new Circle(new Point2D(1, 1), 1).findParametersForGivenSlope(Double.NaN));
        Assert.assertEquals(List.of(0.0, Math.PI), new Circle(new Point2D(1, 1), 2).findParametersForGivenSlope(Double.NaN));
        Assert.assertEquals(List.of(0.5 * Math.PI, 1.5 * Math.PI), new Circle(new Point2D(0, 0), 1).findParametersForGivenSlope(0));

        Assert.assertEquals(List.of(-(45.0 / 180.0) * Math.PI, (135.0 / 180.0) * Math.PI), new Circle(new Point2D(0, 0), 1).findParametersForGivenSlope(1.0));
        Assert.assertEquals(List.of((45.0 / 180.0) * Math.PI, (225.0 / 180.0) * Math.PI), new Circle(new Point2D(0, 0), -1).findParametersForGivenSlope(-1.0));

    }

    @Test
    public void testDoesIntersectWithLine() {
        Assert.assertTrue(new Circle(new Point2D(0, 0), 1).doesIntersect(new Line2D(new Point2D(-10, -10), new Point2D(10, 10))));
        Assert.assertTrue(new Circle(new Point2D(0, 0), 10).doesIntersect(new Line2D(new Point2D(-10, -10), new Point2D(10, 10))));
        Assert.assertTrue(new Circle(new Point2D(1, 1), 10).doesIntersect(new Line2D(new Point2D(-10, -10), new Point2D(10, 10))));
        Assert.assertFalse(new Circle(new Point2D(0, 0), 1).doesIntersect(new Line2D(new Point2D(10, -10), new Point2D(10, 10))));
        Assert.assertFalse(new Circle(new Point2D(0, 0), 1).doesIntersect(new Line2D(new Point2D(10, 10), new Point2D(-10, 10))));
        Assert.assertTrue(new Circle(new Point2D(0, 0), 10).doesIntersect(new Line2D(new Point2D(2, 2), new Point2D(3, 3))));
        Assert.assertTrue(new Circle(new Point2D(0, 0), 10).doesIntersect(new Line2D(new Point2D(0, -0.5), new Point2D(0.5, 0))));
        Assert.assertTrue(new Circle(new Point2D(0, 0), 1).doesIntersect(new Line2D(new Point2D(0, -1), new Point2D(0, 1))));
        Assert.assertTrue(new Circle(new Point2D(0, 0), 1).doesIntersect(new Line2D(new Point2D(0, -1), new Point2D(1, -1))));
    }


    @Test
    public void testFindPointOnCircleClosestToLine() {
        Assert.assertEquals(new Point2D(1, 0), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(1, -1), new Point2D(1, 1))));
        Assert.assertEquals(new Point2D(-1, 0), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(-1, -1), new Point2D(-1, 1))));
        Assert.assertEquals(new Point2D(1, 0), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(10, -1), new Point2D(10, 1))));
        Assert.assertEquals(new Point2D(0, -1), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(-1, -10), new Point2D(1, -10))));
        Assert.assertEquals(new Point2D(-0.5 * Math.sqrt(2), -0.5 * Math.sqrt(2)), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(-1, 0), new Point2D(0, -1))));
        Assert.assertEquals(new Point2D(0.5 * Math.sqrt(2), 0.5 * Math.sqrt(2)), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(10, 0), new Point2D(0, 10))));
        Assert.assertEquals(new Point2D(0, 1), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(1, 1), new Point2D(-1, 1))));
        Assert.assertEquals(new Point2D(0, 1), Circle.UNIT_CIRCLE.findPointOnCircleClosestToLine(new Line2D(new Point2D(1, 1), new Point2D(-1, 1))));
    }

    @Test
    public void testFindLineIntersection() {

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(10, -10), new Point2D(10, 10))));

        Assert.assertEquals(
                Set.of(new Point2D(-1, 0), new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(0, 0), new Point2D(10, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(-10, 0), new Point2D(10, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(1, -1), new Point2D(1, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(-1, -1), new Point2D(1, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(0, -1), new Point2D(0, 1)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(0, -0.5), new Point2D(0, 0.5))));

        Assert.assertEquals(
                Set.of(new Point2D(-1, 0), new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(-0.5, 0), new Point2D(0.5, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(0, 1)),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(1, 0), new Point2D(0, 1))));

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new Line2D(new Point2D(10, 0), new Point2D(0, 10))));
    }

    @Test
    public void testFindLineSegmentIntersection() {
        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(10, -10), new Point2D(10, 10))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(-10, 0), new Point2D(10, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(1, -1), new Point2D(1, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(-1, -1), new Point2D(1, 1))));

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(0, -0.5), new Point2D(0, 0.5))));

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(-0.5, 0), new Point2D(0.5, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(0, 1)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(1, 0), new Point2D(0, 1))));

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(10, 0), new Point2D(0, 10))));

        Assert.assertEquals(
                Set.of(new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(0, 0), new Point2D(10, 10))));

        Assert.assertEquals(
                Set.of(new Point2D(0, 1)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(0, 0.999), new Point2D(0, 1.111))));

        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(0.999, 0), new Point2D(-0.999, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new LineSegment2D(new Point2D(1.999, 0), new Point2D(-1.999, 0))));

    }

    @Test
    public void testFindRayIntersection() {
        Assert.assertEquals(
                Set.of(),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(1, 1), new Vector2D(1, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(-Math.sqrt(1 / 2.0), -Math.sqrt(1 / 2.0)), new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(1, 1), new Vector2D(-1, -1))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0), new Point2D(-1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(1, 0), new Vector2D(-1, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(1, 0), new Vector2D(1, 0))));

        Assert.assertEquals(
                Set.of(new Point2D(1, 0)),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(1, 0), new Vector2D(0, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(Math.sqrt(1 / 2.0), Math.sqrt(1 / 2.0))),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(-0.1, -0.1), new Vector2D(1, 1))));

        Assert.assertEquals(
                Set.of(new Point2D(0, 1)),
                Circle.UNIT_CIRCLE.findIntersection(new Ray2D(new Point2D(-0.5, 0.5), new Vector2D(1, 1))));
    }

    @Test
    public void testPointOnCircleClosestToLine() {
        Assert.assertEquals(
                new Point2D(192.44, 597.56),
                new Circle(new Point2D(192.44, 607.56), 10.0)
                        .findPointOnCircleClosestToLine(new Line2D(new Point2D(200, 600), new Point2D(500, 600))));
    }

}
