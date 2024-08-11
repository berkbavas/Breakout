package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Rectangle2D {
    //
    //                     Top
    //      Left Top                  Right Top
    //          *------------------------*
    //          |                        |
    //    Left  |                        |  Right
    //          |                        |
    //          *------------------------*
    //     Left Bottom              Right Bottom
    //                    Bottom

    private final LineSegment2D left;
    private final LineSegment2D right;
    private final LineSegment2D top;
    private final LineSegment2D bottom;

    private final Point2D leftTop;
    private final Point2D leftBottom;
    private final Point2D rightTop;
    private final Point2D rightBottom;

    private final double width;
    private final double height;

    ArrayList<LineSegment2D> edges = new ArrayList<>();

    public Rectangle2D(double x, double y, double width, double height) {
        leftTop = new Point2D(x, y);
        leftBottom = new Point2D(x, y + height);
        rightTop = new Point2D(x + width, height);
        rightBottom = new Point2D(x + width, y + height);

        left = new LineSegment2D(leftTop, leftBottom);
        right = new LineSegment2D(rightTop, rightBottom);
        top = new LineSegment2D(leftTop, rightTop);
        bottom = new LineSegment2D(leftBottom, rightBottom);

        edges.add(left);
        edges.add(right);
        edges.add(top);
        edges.add(bottom);

        this.width = width;
        this.height = height;
    }

    public Rectangle2D(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom) {
        this.leftTop = leftTop;
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;

        left = new LineSegment2D(leftTop, leftBottom);
        right = new LineSegment2D(rightTop, rightBottom);
        top = new LineSegment2D(leftTop, rightTop);
        bottom = new LineSegment2D(leftBottom, rightBottom);

        edges.add(left);
        edges.add(right);
        edges.add(top);
        edges.add(bottom);

        width = leftTop.distanceTo(rightTop);
        height = leftTop.distanceTo(leftBottom);
    }


    public boolean isPointInside(Point2D point) {
        Vector2D v0 = point.subtract(leftBottom).toVector2D();
        Vector2D v1 = point.subtract(rightTop).toVector2D();
        Vector2D v2 = point.subtract(leftTop).toVector2D();
        Vector2D v3 = point.subtract(rightBottom).toVector2D();

        double dot0 = Vector2D.dot(v0, v1);
        double dot1 = Vector2D.dot(v2, v3);

        return Util.isLessThanOrEqualToZero(dot0) && Util.isLessThanOrEqualToZero(dot1);
    }

    public ArrayList<Point2D> findIntersections(Ray2D ray) {
        ArrayList<Point2D> intersections = new ArrayList<>();

        for (LineSegment2D edge : edges) {
            edge.findIntersection(ray).ifPresent(intersections::add);
        }

        return intersections;
    }

    public static boolean isRectangle2D(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom) {

        LineSegment2D left = new LineSegment2D(leftTop, leftBottom);
        LineSegment2D right = new LineSegment2D(rightTop, rightBottom);
        LineSegment2D top = new LineSegment2D(leftTop, rightTop);
        LineSegment2D bottom = new LineSegment2D(leftBottom, rightBottom);

        Vector2D leftDir = left.direction();
        Vector2D topDir = top.direction();

        Vector2D rightDir = right.direction();
        Vector2D bottomDir = bottom.direction();

        return Util.isFuzzyZero(leftDir.dot(topDir)) &&
                Util.isFuzzyZero(topDir.dot(rightDir)) &&
                Util.isFuzzyZero(rightDir.dot(bottomDir)) &&
                Util.isFuzzyZero(bottomDir.dot(leftDir));
    }
}
