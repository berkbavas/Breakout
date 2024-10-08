package com.github.berkbavas.breakout.math;

import javafx.util.Pair;
import lombok.Getter;

import java.util.Set;

@Getter
public class Point2D {
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double distanceBetween(Point2D a, Point2D b) {
        final double dx = (a.x - b.x);
        final double dy = (a.y - b.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double distanceBetween(Pair<Point2D, Point2D> pair) {
        return distanceBetween(pair.getKey(), pair.getValue());
    }

    public static double angleBetween(Point2D a, Point2D b) {
        double angle = Math.atan2(a.y, a.x) - Math.atan2(b.y, b.x);

        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        } else if (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        }

        return angle;
    }

    public static Point2D findClosestPoint(Point2D subject, Set<Point2D> points) {
        double minDistance = Double.MAX_VALUE;
        Point2D closestPoint = null;

        for (Point2D point : points) {
            double distance = subject.distanceTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }

    public static Pair<Point2D, Point2D> findClosestPair(Set<Pair<Point2D, Point2D>> listOfPairs) {
        Pair<Point2D, Point2D> closestPair = null;
        double minDistance = Double.MAX_VALUE;

        for (Pair<Point2D, Point2D> pair : listOfPairs) {
            double distance = distanceBetween(pair);
            if (distance < minDistance) {
                minDistance = distance;
                closestPair = pair;
            }

        }

        return closestPair;
    }

    public static Pair<Point2D, Point2D> findClosestPairAmongTwoList(Set<Point2D> list0, Set<Point2D> list1) {
        Pair<Point2D, Point2D> closestPair = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2D p0 : list0) {
            for (Point2D p1 : list1) {
                double distance = p0.distanceTo(p1);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPair = new Pair<>(p0, p1);
                }
            }
        }

        return closestPair;
    }

    public Point2D add(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D add(Vector2D other) {
        return new Point2D(x + other.getX(), y + other.getY());
    }

    public Vector2D subtract(Point2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(scalar * x, scalar * y);
    }

    public double distanceTo(Point2D other) {
        return distanceBetween(this, other);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double angleBetween(Point2D other) {
        return angleBetween(this, other);
    }

    public Vector2D toVector2D() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point2D{x = %.2f y = %.2f}", x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Point2D) {
            Point2D other = (Point2D) object;
            return Util.fuzzyCompare(x, other.x) && Util.fuzzyCompare(y, other.y);
        } else {
            return false;
        }
    }

}
