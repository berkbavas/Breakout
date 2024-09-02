package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.SteadyTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

public class VisualDebugger {
    private final static double COLLISION_INDICATION_TIMEOUT_IN_SEC = 0.0;
    private final static double COLLISION_PREDICTION_START_TIME_IN_SEC = 0.35;

    private final GameObjects objects;
    private final PaintCommandHandler[] painter = new PaintCommandHandler[3];
    private final Stopwatch chronometer = new Stopwatch();
    private double sinceCollision = Double.MAX_VALUE;

    public VisualDebugger(GameObjects objects) {
        this.objects = objects;
        painter[0] = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
        painter[1] = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
        painter[2] = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
    }

    public void paint(Tick<? extends Collision> result) {
        painter[0].clear();

        final double minimumTimeToCollision = result.getMinimumTimeToCollision();

        if (result instanceof CrashTick) {
            chronometer.start();
            sinceCollision = 0;
        } else if (result instanceof SteadyTick) {
            painter[0].fill(objects.getBall(), Color.LAWNGREEN);
        }

        if (sinceCollision < COLLISION_INDICATION_TIMEOUT_IN_SEC) {
            sinceCollision = chronometer.getSeconds();
            painter[0].fill(objects.getBall(), Color.RED);
        } else if (minimumTimeToCollision < COLLISION_PREDICTION_START_TIME_IN_SEC) {
            double inv = minimumTimeToCollision / COLLISION_PREDICTION_START_TIME_IN_SEC;
            double alpha = 1 - inv * inv;
            painter[0].fill(objects.getBall(), Color.rgb(255, 0, 0, alpha));
        }
    }

    public void paint(Ball ball) {
        painter[2].clear();
        // Velocity indicator
        Point2D center = ball.getCenter();
        Vector2D velocity = ball.getVelocity();
        Point2D p0 = center.add(velocity.multiply(1 / 10.0));
        painter[2].drawLine(center, p0, Color.CYAN, 2);

        // Acceleration indicator
        Vector2D acceleration = ball.getAcceleration();
        Point2D q0 = center.add(acceleration.normalized().multiply(100));
        painter[2].drawLine(center, q0, Color.MAGENTA, 2);
    }


}
