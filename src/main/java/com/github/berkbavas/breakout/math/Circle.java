package com.github.berkbavas.breakout.math;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
@Getter
public class Circle {
    public static final Circle UNIT_CIRCLE = new Circle(new Point2D(0, 0), 1);

    private final Point2D center;
    private final double radius;

    // A circle C centered at (a,b) with radius r is the set of points (x,y)
    // such that (x-a)^2 + (y-b)^2 = r^2 (Eq. 1),
    // i.e,
    // C = { (x,y) in R | (x-a)^2 + (y-b)^2 = r^2 }.
    //
    // Alternatively, a circle C with radius r centered at (a, b) can be defined
    // by the following parametric equation.
    // C(theta) = (a + r * cos(theta), b + r * sin(theta)), where theta in [0, 2 * PI]. (Eq. 2)
    //
    // Slope of the line tangent to a circle at point (x,y) can be found by
    // taking derivative of (Eq. 1) with respect to x.
    //
    // Dx((x-a)^2 + (y-b)^2) = Dx(r^2) =>
    // 2 * (x - a) + 2 * (y - b) * y' = 0 =>
    // 2 * (y - b) * y' = -2 * (x - a) =>
    // y' = (a - x) / (y - b).
    // Note that the slope is undefined when y = b.
    // When y = b we have x = a + r or x = a -r.
    // So there two points on the circle at which slope of line tangent to circle are undefined.
    //
    // Alternatively, we can find the slope of the line tangent to a circle
    // at t in [0, 2 * PI] by taking component-wise derivative of (Eq. 2) w.r.t to theta.
    // C'(theta) = (-r * sin(theta), r * sin(theta)).
    // So the slope of the line tangent to a circle at t is given by
    // - r * cos(theta) / r * sin(theta) = - cot(theta) = - 1 / tan(theta).
    // Here when theta = 0 or theta = PI the slope is undefined.

    public Circle(Point2D center, double radius) {
        if (radius <= 0.0) {
            throw new IllegalArgumentException(String.format("Radius (%.5f) must be a positive number.", radius));
        }

        this.center = center;
        this.radius = radius;
    }

    // C(theta) = (a, b) + (r * cos(theta), r * sin(theta)), where (a,b) is the center and r is the radius.
    public Point2D calculatePointAt(double theta) {
        return new Point2D(center.x + radius * Math.cos(theta), center.y + radius * Math.sin(theta));
    }

    // C'(theta) = (-r * sin(theta), r * cos(theta)).
    public Vector2D calculateGradientAt(double theta) {
        return new Vector2D(-radius * Math.sin(theta), radius * Math.cos(theta));
    }

    // Rotate gradient vector PI / 2 radians around z-axis counter-clockwise
    public Vector2D calculateNormalAt(double theta) {
        return calculateGradientAt(theta + 0.5 * Math.PI).normalized();
    }

    public double calculateSlopeOfTangent(double theta) {
        final double tan = Math.tan(theta);
        if (Util.isFuzzyZero(tan)) {
            return Double.NaN;
        } else {
            return -1.0 / tan;
        }
    }

    public List<Double> findParametersAtWhichTangentLinesHasGivenSlope(double slope) {
        if (Double.isNaN(slope)) {
            return List.of(0.0, Math.PI);
        } else if (Util.isFuzzyZero(slope)) {
            return List.of(0.5 * Math.PI, 1.5 * Math.PI);
        } else {
            final double theta = Math.atan(-1.0 / slope);
            return List.of(theta, theta + Math.PI);
        }
    }

    // For the given slope, it finds two points where the lines tangent to the circle.
    public List<Point2D> findPointsAtWhichTangentLinesHasGivenSlope(double slope) {
        final List<Double> parameters = findParametersAtWhichTangentLinesHasGivenSlope(slope);
        final Point2D p0 = calculatePointAt(parameters.get(0));
        final Point2D p1 = calculatePointAt(parameters.get(1));
        return List.of(p0, p1);
    }

    public boolean doesIntersect(Line2D line) {
        double slope = line.getSlope();
        Ray2D normalRay = constructNormalRayAtGivenSlope(slope, 0);
        Line2D normalLine = Line2D.from(normalRay);

        var ref = new Object() {
            boolean intersects = false;
        };

        line.findIntersection(normalLine).ifPresent((Point2D point) -> {
            ref.intersects = isPointInsideCircle(point);
        });

        return ref.intersects;
    }

    public boolean doesIntersect(LineSegment2D ls) {
        Ray2D ray = Ray2D.from(ls);

        var ref = new Object() {
            boolean intersects = false;
        };

        ray.findIntersection(this).forEach((Point2D point) -> {
            if (ls.isPointOnLineSegment(point)) {
                ref.intersects = true;
            }
        });

        return ref.intersects;
    }

    public boolean doesIntersect(Ray2D ray) {
        Point2D closestPointToCenter = ray.findClosestPointToCircleCenter(this);
        return isPointInsideCircle(closestPointToCenter);
    }

    // Assumes the given line does not intersect the circle
    // otherwise the result is wrong.
    public Point2D findPointOnCircleClosestToLine(Line2D line) {
        double slope = line.getSlope();
        List<Point2D> points = findPointsAtWhichTangentLinesHasGivenSlope(slope);
        Point2D p0 = points.get(0);
        Point2D p1 = points.get(1);
        double distance0 = line.calculateDistance(p0);
        double distance1 = line.calculateDistance(p1);

        if (distance0 < distance1) {
            return p0;
        } else {
            return p1;
        }
    }

    // Assumes the given line does not intersect the circle
    // otherwise the result is wrong.
    public Point2D findPointOnLineClosestToCircle(Line2D line) {
        Point2D pointOnCircleClosestToLine = findPointOnCircleClosestToLine(line);
        Vector2D centerToPoint = pointOnCircleClosestToLine.subtract(center);
        Ray2D ray = new Ray2D(pointOnCircleClosestToLine, centerToPoint);
        Optional<Point2D> optional = line.findIntersection(ray);
        assert optional.isPresent();
        return optional.get();
    }

    public boolean isPointOnCircle(Point2D point) {
        double dx = center.x - point.x;
        double dy = center.y - point.y;
        return Util.fuzzyCompare(radius * radius, dx * dx + dy * dy);
    }

    public boolean isPointInsideCircle(Point2D point) {
        double dx = center.x - point.x;
        double dy = center.y - point.y;
        return Util.isFuzzyBetween(0.0, dx * dx + dy * dy, radius * radius);
    }

    public Ray2D constructTangentRayAtGivenSlope(double slope, int whichRay) {
        List<Double> parameters = findParametersAtWhichTangentLinesHasGivenSlope(slope);
        double parameter = parameters.get(whichRay);
        Point2D origin = calculatePointAt(parameter);
        Vector2D direction = calculateGradientAt(parameter);
        return new Ray2D(origin, direction);
    }

    public Ray2D constructNormalRayAtGivenSlope(double slope, int whichRay) {
        List<Double> parameters = findParametersAtWhichTangentLinesHasGivenSlope(slope);
        double parameter = parameters.get(whichRay);
        Point2D origin = calculatePointAt(parameter);
        Vector2D direction = calculateNormalAt(parameter);
        return new Ray2D(origin, direction);
    }

    public List<Point2D> findIntersection(Line2D line) {
        Point2D origin = line.getQ();

        // We assume direction is normalized, i.e., has length 1.
        Vector2D direction = new Vector2D(line.getQ().x - line.getP().x, line.getQ().y - line.getP().y).normalized();

        Vector2D originToCenter = center.subtract(origin);
        double dot = direction.dot(originToCenter);
        Point2D closestPointToCenter = origin.add(direction.multiply(dot));

        if (isPointOnCircle(closestPointToCenter)) {
            // Line is tangent to the circle at [closestPointToCenter]
            return List.of(closestPointToCenter);
        } else if (isPointInsideCircle(closestPointToCenter)) {
            double distanceToCenter = center.distanceTo(closestPointToCenter);
            double distanceToIntersectionPoint = Math.sqrt(radius * radius - distanceToCenter * distanceToCenter);

            Point2D p0 = closestPointToCenter.add(direction.multiply(distanceToIntersectionPoint));
            Point2D p1 = closestPointToCenter.add(direction.multiply(-distanceToIntersectionPoint));
            return List.of(p0, p1);
        }

        return List.of();
    }

    public List<Point2D> findIntersection(Ray2D ray) {
        ArrayList<Point2D> list = new ArrayList<>();
        Line2D line = Line2D.from(ray);

        findIntersection(line).forEach(
                (Point2D point) -> {
                    if (ray.isPointOnRay(point)) {
                        list.add(point);
                    }
                }
        );

        return list;
    }

    public Optional<Point2D> findIntersectionClosestToOriginOfRay(Ray2D ray) {
        List<Point2D> intersections = findIntersection(ray);

        double minDistance = Double.MAX_VALUE;
        Point2D pointOnCircleClosestToOriginOfRay = null;

        for (Point2D point : intersections) {
            double distance = point.distanceTo(ray.getOrigin());
            if (distance < minDistance) {
                minDistance = distance;
                pointOnCircleClosestToOriginOfRay = point;
            }
        }

        if (pointOnCircleClosestToOriginOfRay != null) {
            return Optional.of(pointOnCircleClosestToOriginOfRay);
        }

        return Optional.empty();
    }

    public List<Point2D> findIntersection(LineSegment2D lineSegment) {
        ArrayList<Point2D> list = new ArrayList<>();
        Line2D line = Line2D.from(lineSegment);

        findIntersection(line).forEach(
                (Point2D point) -> {
                    if (lineSegment.isPointOnLineSegment(point)) {
                        list.add(point);
                    }
                }
        );

        return list;
    }
}
