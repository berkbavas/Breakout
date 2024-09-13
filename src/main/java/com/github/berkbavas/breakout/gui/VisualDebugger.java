package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.StationaryTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

public class VisualDebugger {
    private final static double COLLISION_INDICATION_TIMEOUT_IN_SEC = 0.0;
    private final static double COLLISION_PREDICTION_START_TIME_IN_SEC = 0.35;

    private final Ball ball;
    private final PaintCommandHandler handler;
    private final Stopwatch chronometer = new Stopwatch();
    private double sinceCollision = Double.MAX_VALUE;

    public VisualDebugger(Ball ball, PaintCommandHandler handler) {
        this.ball = ball;
        this.handler = handler;
    }

    public void clear() {
        handler.clear();
    }

    public void paint(Tick<? extends Collision> result) {
        final double minimumTimeToCollision = result.getMinimumTimeToCollision();

        if (result instanceof CrashTick) {
            chronometer.start();
            sinceCollision = 0;
        } else if (result instanceof StationaryTick) {
            handler.fill(ball, Color.LAWNGREEN);
        }

        if (sinceCollision < COLLISION_INDICATION_TIMEOUT_IN_SEC) {
            sinceCollision = chronometer.getSeconds();
            handler.fill(ball, Color.RED);
        } else if (minimumTimeToCollision < COLLISION_PREDICTION_START_TIME_IN_SEC) {
            double inv = minimumTimeToCollision / COLLISION_PREDICTION_START_TIME_IN_SEC;
            double alpha = 1 - inv * inv;
            handler.fill(ball, Color.rgb(255, 0, 0, alpha));
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
