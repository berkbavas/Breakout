package com.github.berkbavas.breakout.math;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Getter(AccessLevel.NONE)
    HashSet<LineSegment2D> edges = new HashSet<>();

    @Getter(AccessLevel.NONE)
    HashSet<Point2D> vertices = new HashSet<>();

    public Rectangle2D(double x, double y, double width, double height) {
        this(
                new Point2D(x, y),
                new Point2D(x, y + height),
                new Point2D(x + width, y),
                new Point2D(x + width, y + height)
        );
    }

    public Rectangle2D(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom) {
        this.leftTop = leftTop;
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;

        // Be aware of the order of vertices.
        // It affects the direction of normal.
        left = new LineSegment2D(leftTop, leftBottom, "[Left]");
        right = new LineSegment2D(rightBottom, rightTop, "[Right]");
        top = new LineSegment2D(rightTop, leftTop, "[Top]");
        bottom = new LineSegment2D(leftBottom, rightBottom, "[Bottom]");

        width = leftTop.distanceTo(rightTop);
        height = leftTop.distanceTo(leftBottom);

        constructVertexAndEdgeSet();
    }


    public Set<LineSegment2D> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public Set<Point2D> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    public boolean collides(Rectangle2D other) {
        for (LineSegment2D edge : edges) {
            for (LineSegment2D otherEdge : other.edges) {
                if (edge.findIntersection(otherEdge).isPresent()) {
                    return true;
                }
            }
        }

        for (Point2D vertex : other.vertices) {
            if (this.contains(vertex)) {
                return true;
            }
        }

        for (Point2D vertex : vertices) {
            if (other.contains(vertex)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(Point2D point) {
        Vector2D v0 = point.subtract(leftBottom).toVector2D();
        Vector2D v1 = point.subtract(rightTop).toVector2D();
        double dot0 = Vector2D.dot(v0, v1);

        if (!Util.isLessThanOrEqualToZero(dot0)) {
            return false;
        }

        Vector2D v2 = point.subtract(leftTop).toVector2D();
        Vector2D v3 = point.subtract(rightBottom).toVector2D();
        double dot1 = Vector2D.dot(v2, v3);

        return Util.isLessThanOrEqualToZero(dot1);
    }

    public Set<Point2D> findIntersections(Rectangle2D other) {
        Set<Point2D> intersections = new HashSet<>();

        for (LineSegment2D edge : edges) {
            for (LineSegment2D otherEdge : other.edges) {
                edge.findIntersection(otherEdge).ifPresent(intersections::add);
            }
        }

        return intersections;
    }

    public Set<Point2D> findIntersections(LineSegment2D ls) {
        Set<Point2D> intersections = new HashSet<>();

        for (LineSegment2D edge : edges) {
            edge.findIntersection(ls).ifPresent(intersections::add);
        }

        return intersections;
    }

    public Set<Point2D> findIntersections(Ray2D ray) {
        Set<Point2D> intersections = new HashSet<>();

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

        Vector2D leftDir = left.getDirection();
        Vector2D topDir = top.getDirection();

        Vector2D rightDir = right.getDirection();
        Vector2D bottomDir = bottom.getDirection();

        return Util.isFuzzyZero(leftDir.dot(topDir)) &&
                Util.isFuzzyZero(topDir.dot(rightDir)) &&
                Util.isFuzzyZero(rightDir.dot(bottomDir)) &&
                Util.isFuzzyZero(bottomDir.dot(leftDir));
    }

    private void constructVertexAndEdgeSet() {
        edges.add(left);
        edges.add(right);
        edges.add(top);
        edges.add(bottom);

        vertices.add(leftTop);
        vertices.add(rightTop);
        vertices.add(leftBottom);
        vertices.add(rightBottom);
    }
}
