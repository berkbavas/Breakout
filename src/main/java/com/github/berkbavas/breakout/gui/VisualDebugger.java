package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.StationaryTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public class VisualDebugger {
    public final static SimpleBooleanProperty ENABLED = new SimpleBooleanProperty(true);

    private final static double INDICATE_COLLISION_DISTANCE = 100; // In world's distance unit.

    private final Ball ball;
    private final PaintCommandHandler handler;

    public VisualDebugger(Ball ball, PaintCommandHandler handler) {
        this.ball = ball;
        this.handler = handler;

    }

    public void clear() {
        handler.clear();
    }

    public void paint(Tick<? extends Collision> result) {
        if (!ENABLED.get()) {
            return;
        }

        if (result instanceof StationaryTick) {
            handler.fill(ball, Color.LAWNGREEN);
        }

        var minimumDistance = result.getMinimumDistanceToCollision();

        if (minimumDistance < INDICATE_COLLISION_DISTANCE) {
            double normalized = minimumDistance / INDICATE_COLLISION_DISTANCE;
            handler.fill(ball, Color.rgb(255, 0, 0, 1 - normalized));
        }
    }

    public void paint() {
        if (!ENABLED.get()) {
            return;
        }

        // Velocity indicator
        Point2D center = ball.getCenter();
        if (ball.getSpeed() != 0) {
            Vector2D velocity = ball.getVelocity();
            Point2D p0 = center.add(velocity.multiply(0.1));
            handler.drawLine(center, p0, Color.CYAN, 2);
        }

        // Net force indicator
        Vector2D netForce = ball.getNetForce();
        if (netForce.length() != 0) {
            Point2D q0 = center.add(netForce);
            handler.drawLine(center, q0, Color.MAGENTA, 2);
        }
    }

}
