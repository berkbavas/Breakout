package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.Collision;
import com.github.berkbavas.breakout.physics.TickProcessor;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Collider;
import com.github.berkbavas.breakout.physics.node.Draggable;
import com.github.berkbavas.breakout.physics.node.Paddle;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DragEventHandler implements EventListener {
    protected final GameObjects objects;
    private final AtomicBoolean dragLater = new AtomicBoolean(false);

    private Draggable target = null;
    private Point2D delta = null;

    public DragEventHandler(GameObjects objects) {
        this.objects = objects;
    }

    public void update() {
        if (dragLater.get()) {
            translate(target, delta);
            dragLater.set(false);
        }
    }

    protected void dragLater(Draggable target, Point2D delta) {
        this.target = target;
        this.delta = delta;
        dragLater.set(true);
    }

    public void translate(Draggable node, Point2D delta) {
        Ball ball = objects.getBall();

        // Find the closest contacts on collider and ball.
        Set<Pair<Point2D, Point2D>> contacts = new HashSet<>();
        Vector2D velocity = delta.multiply(-1);
        Set<Collision> collisions;

        if (node instanceof Collider) {
            // findCollision() method assumes that ball is moving and collider is steady.
            // But here we would like to find collisions between still ball and moving collider.
            // Hence, we set velocity as -delta.

            Collider collider = (Collider) node;
            collisions = collider.findCollisions(ball, velocity);
            for (Collision collision : collisions) {
                contacts.add(new Pair<>(collision.getContactPointOnEdge(), collision.getContactPointOnBall()));
            }
        }

        Pair<Point2D, Point2D> closest = Point2D.findClosestPair(contacts);

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
