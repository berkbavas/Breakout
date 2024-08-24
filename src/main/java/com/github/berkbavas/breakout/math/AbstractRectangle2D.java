package com.github.berkbavas.breakout.math;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class AbstractRectangle2D<T extends LineSegment2D> extends AbstractPolygon2D<T> {
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

    private Point2D leftTop;
    private Point2D leftBottom;
    private Point2D rightTop;
    private Point2D rightBottom;

    private double width;
    private double height;

    private double x;
    private double y;

    public AbstractRectangle2D(double x, double y, double width, double height) {
        super(List.of(
                        new Point2D(x, y),
                        new Point2D(x, y + height),
                        new Point2D(x + width, y + height),
                        new Point2D(x + width, y)),
                List.of("[Left]", "[Bottom]", "[Right]", "[Top]"));

        construct(x, y, width, height);
    }

    private void construct(double x, double y, double width, double height) {
        this.leftTop = new Point2D(x, y);
        this.leftBottom = new Point2D(x, y + height);
        this.rightBottom = new Point2D(x + width, y + height);
        this.rightTop = new Point2D(x + width, y);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void translate(Point2D delta) {
        super.translate(delta);
        construct(x + delta.getX(), y + delta.getY(), width, height);
    }

    public void translate(double x, double y) {
        translate(new Point2D(x, y));
    }

    public boolean collides(AbstractRectangle2D<T> other) {
        for (T edge : edges) {
            for (T otherEdge : other.edges) {
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
}
