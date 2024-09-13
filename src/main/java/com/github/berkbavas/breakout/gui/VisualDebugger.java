package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.StationaryTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import javafx.scene.paint.Color;

public class VisualDebugger {
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

        // Velocity indicator
        Point2D center = ball.getCenter();
        if (ball.getSpeed() != 0) {
            Vector2D velocity = ball.getVelocity();
            Point2D p0 = center.add(velocity.multiply(0.1));
            handler.drawLine(center, p0, Color.CYAN, 2);
        }

        // Acceleration indicator
        Vector2D pull = ball.getPull();
        if (pull.length() != 0) {
            Point2D q0 = center.add(pull.multiply(0.1));
            handler.drawLine(center, q0, Color.MAGENTA, 2);
        }

        // Resistance indicator
        Vector2D resistance = ball.getResistance();
        if (resistance.length() != 0) {
            Point2D r0 = center.add(resistance.multiply(1));
            handler.drawLine(center, r0, Color.RED, 2);
        }
    }

}
