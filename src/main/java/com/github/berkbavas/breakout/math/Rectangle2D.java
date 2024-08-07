package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Rectangle2D {
    private final LineSegment2D left;
    private final LineSegment2D right;
    private final LineSegment2D top;
    private final LineSegment2D bottom;

    private final float width;
    private final float height;

    ArrayList<LineSegment2D> edges = new ArrayList<>();

    public Rectangle2D(float x, float y, float width, float height) {
        Point2D leftTop = new Point2D(x, y);
        Point2D leftBottom = new Point2D(x, y + height);
        Point2D rightTop = new Point2D(x + width, height);
        Point2D rightBottom = new Point2D(x + width, y + height);

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

    public ArrayList<Point2D> findIntersections(Ray2D ray) {
        ArrayList<Point2D> intersections = new ArrayList<>();

        for (LineSegment2D edge : edges) {
            edge.findIntersection(ray).ifPresent(intersections::add);
        }

        return intersections;
    }
}
