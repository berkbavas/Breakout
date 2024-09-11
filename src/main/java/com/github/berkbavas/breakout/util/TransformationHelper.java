package com.github.berkbavas.breakout.util;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.World;
import javafx.scene.Node;

public class TransformationHelper {
    private static TransformationHelperInner IMPL;

    private TransformationHelper() {
    }

    static public void initialize(World world, Node node) {
        IMPL = new TransformationHelperInner(world, node);
    }

    static public Point2D fromWorldToCanvas(double x, double y) {
        return IMPL.fromWorldToCanvas(x, y);
    }

    static public Point2D fromWorldToCanvas(Point2D p) {
        return IMPL.fromWorldToCanvas(p);
    }

    static public Point2D fromCanvasToWorld(double x, double y) {
        return IMPL.fromCanvasToWorld(x, y);
    }

    static public Point2D fromCanvasToWorld(Point2D p) {
        return IMPL.fromCanvasToWorld(p);
    }

    static public Point2D getCanvasCenter() {
        return IMPL.getCanvasCenter();
    }

    static public Point2D getWorldCenter(Point2D p) {
        return IMPL.getWorldCenter();
    }

    private static class TransformationHelperInner {
        private final World world;
        private final Node node;

        TransformationHelperInner(World world, Node node) {
            this.world = world;
            this.node = node;
        }

        Point2D fromWorldToCanvas(double x, double y) {
            double ww = world.getWidth();
            double wh = world.getHeight();
            double gw = node.getLayoutBounds().getWidth();
            double gh = node.getLayoutBounds().getHeight();

            double nx = x / ww; // [0, 1]
            double ny = y / wh; // [0, 1]

            return new Point2D(nx * gw, ny * gh);
        }

        Point2D fromWorldToCanvas(Point2D p) {
            return fromWorldToCanvas(p.getX(), p.getY());
        }

        Point2D fromCanvasToWorld(double x, double y) {
            double ww = world.getWidth();
            double wh = world.getHeight();
            double gw = node.getLayoutBounds().getWidth();
            double gh = node.getLayoutBounds().getHeight();

            double nx = x / gw; // [0, 1]
            double ny = y / gh; // [0, 1]

            return new Point2D(nx * ww, ny * wh);
        }

        Point2D fromCanvasToWorld(Point2D p) {
            return fromCanvasToWorld(p.getX(), p.getY());
        }

        public Point2D getWorldCenter() {
            double ww = world.getWidth();
            double wh = world.getHeight();

            return new Point2D(0.5 * ww, 0.5 * wh);
        }

        public Point2D getCanvasCenter() {
            double gw = node.getLayoutBounds().getWidth();
            double gh = node.getLayoutBounds().getHeight();

            return new Point2D(0.5 * gw, 0.5 * gh);
        }
    }

}
