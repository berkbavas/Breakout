package com.github.berkbavas.breakout.math;

import com.github.berkbavas.breakout.util.ReturnValue;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ToString
@Getter
public class Circle {
    public static final Circle UNIT_CIRCLE = new Circle(new Point2D(0, 0), 1);

    protected Point2D center;
    protected double radius;

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
        this.center = center;
        this.radius = radius;
    }

    // C(theta) = (a, b) + (r * cos(theta), r * sin(theta)), where (a,b) is the center and r is the radius.
    public Point2D calculatePointAt(double theta) {
        double x = center.getX() + radius * Math.cos(theta);
        double y = center.getY() + radius * Math.sin(theta);

        return new Point2D(x, y);
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

    public List<Double> findParametersForGivenSlope(double slope) {
        if (Double.isNaN(slope)) {
            return List.of(0.0, Math.PI);
        } else if (Util.isFuzzyZero(slope)) {
            return List.of(0.5 * Math.PI, 1.5 * Math.PI);
        } else {
            final double theta = Math.atan(-1.0 / slope);
            return List.of(theta, theta + Math.PI);
        }
    }

    // Finds two points at which the lines tangent to the circle has given slope.
    public List<Point2D> findPointsForGivenSlope(double slope) {
        final List<Double> parameters = findParametersForGivenSlope(slope);
        final Point2D p0 = calculatePointAt(parameters.get(0));
        final Point2D p1 = calculatePointAt(parameters.get(1));
        return List.of(p0, p1);
    }

    public boolean doesIntersect(Line2D line) {
        var ref = new ReturnValue<>(false);
        List<Point2D> points = findPointsForGivenSlope(line.getSlope());
        Line2D perpendicularLine = new Line2D(points.get(0), points.get(1));
        perpendicularLine.findIntersection(line).ifPresent(intersection -> ref.value = isPointInsideCircle(intersection));
        return ref.value;
    }

    // Assumes the given line does not intersect the circle
    // otherwise the result is wrong.
    public Point2D findPointOnCircleClosestToLine(Line2D line) {
        double slope = line.getSlope();
        List<Point2D> points = findPointsForGivenSlope(slope);
        Point2D p0 = points.get(0);
        Point2D p1 = points.get(1);
        double distance0 = line.calculateDistanceToPoint(p0);
        double distance1 = line.calculateDistanceToPoint(p1);

        if (distance0 < distance1) {
            return p0;
        } else {
            return p1;
        }
    }

    public boolean isPointOnCircle(Point2D point) {
        double dx = center.getX() - point.getX();
        double dy = center.getY() - point.getY();
        return radius * radius == dx * dx + dy * dy;
    }

    public boolean isPointInsideCircle(Point2D point) {
        double dx = center.getX() - point.getX();
        double dy = center.getY() - point.getY();
        return dx * dx + dy * dy <= radius * radius;
    }

    public List<Point2D> findIntersection(Line2D line) {
        Point2D vertex = line.getQ();
        Vector2D direction = line.getDirection();
        Vector2D vertexToCenter = center.subtract(vertex);
        double dot = direction.dot(vertexToCenter);

        // Point on the line closest to the center of circle
        Point2D closestPointToCenter = vertex.add(direction.multiply(dot));

        if (isPointOnCircle(closestPointToCenter)) {
            // Line is tangent to the circle
            return List.of(closestPointToCenter);

        } else if (isPointInsideCircle(closestPointToCenter)) {
            // Line is secant to the circle
            double distanceToCenter = center.distanceTo(closestPointToCenter);
            double distanceToIntersectionPoint = Math.sqrt(radius * radius - distanceToCenter * distanceToCenter);
            Point2D p0 = closestPointToCenter.add(direction.multiply(distanceToIntersectionPoint));
            Point2D p1 = closestPointToCenter.add(direction.multiply(-distanceToIntersectionPoint));

            return List.of(p0, p1);
        }

        // No intersection
        return List.of();
    }

    public Set<Point2D> findIntersection(Ray2D ray) {
        Set<Point2D> result = new HashSet<>();
        Line2D line = Line2D.from(ray);

        findIntersection(line).forEach((Point2D intersection) -> {
            if (ray.isPointOnRay(intersection)) {
                result.add(intersection);
            }
        });

        return result;
    }

    public Optional<Point2D> findIntersectionClosestToRayOrigin(Ray2D ray) {
        Set<Point2D> intersections = findIntersection(ray);
        return Optional.ofNullable(Point2D.findClosestPoint(ray.getOrigin(), intersections));
    }

    public Set<Point2D> findIntersection(LineSegment2D ls) {
        Set<Point2D> result = new HashSet<>();
        Line2D line = Line2D.from(ls);

        findIntersection(line).forEach((Point2D intersection) -> {
            if (ls.isPointOnLineSegment(intersection)) {
                result.add(intersection);
            }
        });

        return result;
    }

    public Circle enlarge(double factor) {
        return new Circle(center, (1 + factor) * radius);
    }
}
