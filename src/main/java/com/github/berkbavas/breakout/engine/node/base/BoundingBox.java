package com.github.berkbavas.breakout.engine.node.base;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Ray2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.Collection;
import java.util.HashMap;

public class BoundingBox {

    private final Ball ball;
    private final HashMap<Vertex.Type, Ray2D> rays = new HashMap<>();
    private final HashMap<Vertex.Type, Point2D> vertices = new HashMap<>();

    public BoundingBox(Ball ball) {
        this.ball = ball;

        constructVertices();
        constructRays();
    }

    public HashMap<Vertex.Type, Ray2D> getCriticalRays() {
        HashMap<Vertex.Type, Ray2D> criticalRays = new HashMap<>();

        final Vector2D velocity = ball.getVelocity();

        vertices.forEach((Vertex.Type type, Point2D vertex) -> {
            final Vector2D normal = Vertex.NORMALS.get(type);
            final float dot = normal.dot(velocity);
            final boolean isCritical = Util.isGreaterThanOrEqualToZero(dot);
            if (isCritical) {
                criticalRays.put(type, rays.get(type));
            }
        });

        return criticalRays;
    }

    Point2D getVertex(Vertex.Type type) {
        return vertices.get(type);
    }

    Ray2D getRay(Vertex.Type type) {
        return rays.get(type);
    }

    public Collection<Point2D> getVertices() {
        return vertices.values();
    }

    private void constructVertices() {
        final float cx = ball.getCenter().getX();
        final float cy = ball.getCenter().getY();
        final float r = ball.getRadius();

        Point2D left = new Point2D(cx - r, cy);
        Point2D right = new Point2D(cx + r, cy);
        Point2D top = new Point2D(cx, cy - r);
        Point2D bottom = new Point2D(cx, cy + r);
        Point2D center = new Point2D(cx, cy);

        Point2D leftTop = new Point2D(cx - r, cy - r);
        Point2D rightTop = new Point2D(cx + r, cy - r);
        Point2D leftBottom = new Point2D(cx - r, cy + r);
        Point2D rightBottom = new Point2D(cx + r, cy + r);

        vertices.put(Vertex.Type.LEFT, left);
        vertices.put(Vertex.Type.RIGHT, right);
        vertices.put(Vertex.Type.TOP, top);
        vertices.put(Vertex.Type.BOTTOM, bottom);
    }

    private void constructRays() {
        Vector2D vel = ball.getVelocity();

        vertices.forEach((Vertex.Type type, Point2D vertex) -> {
            rays.put(type, new Ray2D(vertex, vel));
        });
    }

}
