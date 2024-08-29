package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Paddle;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.Vertex;
import com.github.berkbavas.breakout.physics.simulator.core.CollisionEngine;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class DragEventHandler implements EventListener {
    protected final GameObjects objects;

    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

    public DragEventHandler(GameObjects objects) {
        this.objects = objects;
    }

    public void update() {
        if (!queue.isEmpty()) {
            queue.remove().run();
        }
    }

    protected void dragLater(Draggable target, Point2D delta) {
        queue.add(() -> translate(target, delta));
    }

    public void translate(Draggable node, Point2D delta) {
        Ball ball = objects.getBall();

        Pair<Point2D, Point2D> closest = null;
        // Find the closest points on collider and ball, if the draggable is a collider as well.
        if (node instanceof Collider) {
            // findCollision() method assumes that ball is moving and collider is steady.
            // But here we would like to find collisions between still ball and moving collider.
            // Hence, we set velocity as -delta.
            Vector2D velocity = delta.multiply(-1);
            Collider collider = (Collider) node;

            Set<Pair<Point2D, Vertex>> pairs = CollisionEngine.findClosestPairsAlongGivenDirection(ball, collider, velocity);
            closest = Vertex.findClosestPair(new ArrayList<>(pairs));
        }

        Point2D allowedTranslation;

        if (closest == null) {
            // No collision along the delta position.
            // We are good to translate the node without concerning the ball vs node collision.
            allowedTranslation = delta;
        } else {
            Point2D contactPointOnEdge = closest.getKey();
            Point2D contactPointOnBall = closest.getValue();
            allowedTranslation = calculateAllowedTranslation(contactPointOnEdge, contactPointOnBall, delta);
        }

        // Translate now.
        if (node instanceof Paddle) {
            // Paddle is allowed to move on x-axis only, take care of this case.
            Paddle paddle = (Paddle) node;
            double paddleLeftCurrent = paddle.getX();
            double paddleLeftRequestedDelta = allowedTranslation.getX();
            double paddleLeftMin = 0;
            double paddleLeftMax = objects.getWorld().getWidth() - paddle.getWidth();
            double paddleLeftRequested = paddleLeftCurrent + paddleLeftRequestedDelta;
            double paddleLeftClamped = Util.clamp(paddleLeftMin, paddleLeftRequested, paddleLeftMax);
            double paddleLeftClampedDelta = paddleLeftClamped - paddleLeftCurrent;
            paddle.translate(paddleLeftClampedDelta, 0);

        } else {
            node.translate(allowedTranslation);
        }
    }

    private Point2D calculateAllowedTranslation(Point2D contactPointOnEdge, Point2D contactPointOnBall, Point2D delta) {
        Vector2D maximumAllowedTranslation = contactPointOnBall.subtract(contactPointOnEdge);
        Vector2D requestedTranslation = delta.toVector2D();
        Vector2D projection = maximumAllowedTranslation.projectOnto(requestedTranslation);
        double maxAllowedTranslationDistance = projection.length();
        double requestedTranslationDistance = requestedTranslation.length();
        double allowedTranslationDistance =
                Util.clamp(0, requestedTranslationDistance, maxAllowedTranslationDistance - 1);
        Vector2D translationDirection = requestedTranslation.normalized();
        return translationDirection.multiply(allowedTranslationDistance).toPoint2D();
    }
}
