package com.github.berkbavas.breakout.util;

import com.github.berkbavas.breakout.math.Point2D;
import lombok.Getter;
import lombok.Setter;

public class TransformationHelper {
    private static TransformationHelperInner IMPL;

    private TransformationHelper() {

    }

    static public void initialize(double ww, double wh, double sw, double sh) {
        IMPL = new TransformationHelperInner(ww, wh, sw, sh);
    }

    static public void setScale(double scale) {
        IMPL.setScale(scale);
    }

    static public Point2D fromWorldToScene(double x, double y) {
        return IMPL.fromWorldToScene(x, y);
    }

    static public Point2D fromWorldToScene(Point2D p) {
        return IMPL.fromWorldToScene(p);
    }

    static public Point2D fromSceneToWorld(double x, double y) {
        return IMPL.fromSceneToWorld(x, y);
    }

    static public Point2D fromSceneToWorld(Point2D p) {
        return IMPL.fromSceneToWorld(p);
    }

    static public Point2D getSceneCenter() {
        return IMPL.getSceneCenter();
    }

    static public Point2D getWorldCenter(Point2D p) {
        return IMPL.getWorldCenter();
    }

    private static class TransformationHelperInner {
        // World size width x height
        private final double ww;
        private final double wh;

        // Scene size width x height
        private final double sw;
        private final double sh;

        @Getter
        private final Point2D sceneCenter;

        @Getter
        private final Point2D worldCenter;

        @Setter
        private double scale = 1.0;

        TransformationHelperInner(double ww, double wh, double sw, double sh) {
            this.ww = ww;
            this.wh = wh;
            this.sw = sw;
            this.sh = sh;
            this.sceneCenter = new Point2D(0.5 * sw, 0.5 * sh);
            this.worldCenter = new Point2D(0.5 * ww, 0.5 * wh);
        }

        Point2D fromWorldToScene(double x, double y) {
            final double nx = x / ww;
            final double ny = y / wh;
            return new Point2D(nx * sw * scale, ny * sh * scale);
        }

        Point2D fromWorldToScene(Point2D p) {
            return fromWorldToScene(p.getX(), p.getY());
        }

        Point2D fromSceneToWorld(double x, double y) {
            final double nx = x / sw;
            final double ny = y / sh;
            return new Point2D(nx * ww / scale, ny * wh / scale);
        }

        Point2D fromSceneToWorld(Point2D p) {
            return fromSceneToWorld(p.getX(), p.getY());
        }

    }

}
