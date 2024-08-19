package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.List;

@Getter
public class Rectangle2D extends Polygon2D {
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

    private final Point2D leftTop;
    private final Point2D leftBottom;
    private final Point2D rightTop;
    private final Point2D rightBottom;

    private final double width;
    private final double height;

    private final double x;
    private final double y;

    public Rectangle2D(double x, double y, double width, double height) {
        super(List.of(
                        new Point2D(x, y),
                        new Point2D(x, y + height),
                        new Point2D(x + width, y + height),
                        new Point2D(x + width, y)),
                List.of("[Left]", "[Bottom]", "[Right]", "[Top]"));

        this.leftTop = new Point2D(x, y);
        this.leftBottom = new Point2D(x, y + height);
        this.rightBottom = new Point2D(x + width, y + height);
        this.rightTop = new Point2D(x + width, y);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
        Vector2D v0 = point.subtract(leftBottom);
        Vector2D v1 = point.subtract(rightTop);
        double dot0 = Vector2D.dot(v0, v1);

        if (!Util.isLessThanOrEqualToZero(dot0)) {
            return false;
        }

        Vector2D v2 = point.subtract(leftTop);
        Vector2D v3 = point.subtract(rightBottom);
        double dot1 = Vector2D.dot(v2, v3);

        return Util.isLessThanOrEqualToZero(dot1);
    }
}
