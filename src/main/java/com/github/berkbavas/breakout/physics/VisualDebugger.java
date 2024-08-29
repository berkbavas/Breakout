package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.core.Collision;
import com.github.berkbavas.breakout.physics.simulator.core.TickResult;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

import java.util.Set;

public class VisualDebugger {
    private final static double COLLISION_INDICATION_TIMEOUT_IN_SEC = 0.0;
    private final static double COLLISION_PREDICTION_START_TIME_IN_SEC = 0.25;

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

    public void paint(TickResult result) {
        painter[0].clear();

        final TickResult.Status status = result.getStatus();
        final double minimumTimeToCollision = result.getMinimumTimeToCollision();

        if (status == TickResult.Status.COLLIDED) {
            chronometer.start();
            sinceCollision = 0;
        } else if (status == TickResult.Status.STEADY) {
            painter[0].fill(objects.getBall(), Color.GREEN);
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

    public void paint(Set<Collision> collisions) {
        // Line(s) between potential collision contacts
        painter[1].clear();

        for (Collision collision : collisions) {
            Point2D p0 = collision.getContactPointOnBall();
            Point2D p1 = collision.getContactPointOnEdge();
            painter[1].drawLine(p0, p1, Color.RED);
        }
    }

    public void paint(Ball ball) {
        painter[2].clear();

        // Velocity indicator
        Point2D center = ball.getCenter();
        Vector2D velocity = ball.getVelocity();
        Point2D p0 = center.add(velocity.multiply(1 / 10.0));
        painter[2].drawLine(center, p0, Color.CYAN, 1);

        // Acceleration indicator
        Vector2D acceleration = ball.getAcceleration();
        Point2D q0 = center.add(acceleration.normalized().multiply(100));
        painter[2].drawLine(center, q0, Color.MAGENTA, 1);
    }

}
