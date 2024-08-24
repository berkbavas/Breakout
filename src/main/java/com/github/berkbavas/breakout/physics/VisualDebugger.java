package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

import java.util.Set;

public class VisualDebugger {
    private final GameObjects objects;
    private final PaintCommandHandler[] painter = new PaintCommandHandler[2];
    private final Stopwatch chronometer = new Stopwatch();
    private double sinceCollision = Double.MAX_VALUE;
    private final static double COLLISION_INDICATION_TIMEOUT_IN_SEC = 0.0;
    private final static double COLLISION_PREDICTION_START_TIME_IN_SEC = 0.25;

    public VisualDebugger(GameObjects objects) {
        this.objects = objects;
        painter[0] = OnDemandPaintCommandProcessor.getPaintCommandHandler(new Object());
        painter[1] = OnDemandPaintCommandProcessor.getPaintCommandHandler(new Object());
    }

    public void paint(TickResult result) {
        final boolean isCollided = result.isCollided();
        final double minimumTimeToCollision = result.getMinimumTimeToCollision();

        if (isCollided) {
            chronometer.start();
            sinceCollision = 0;
        }

        painter[0].clear();

        if (sinceCollision < COLLISION_INDICATION_TIMEOUT_IN_SEC) {
            sinceCollision = chronometer.getSeconds();
            painter[0].fillCircle(objects.getBall(), Color.RED);
        } else if (minimumTimeToCollision < COLLISION_PREDICTION_START_TIME_IN_SEC) {
            double inv = minimumTimeToCollision / COLLISION_PREDICTION_START_TIME_IN_SEC;
            double alpha = 1 - inv * inv;
            painter[0].fillCircle(objects.getBall(), Color.rgb(255, 0, 0, alpha));
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

        // Velocity indicator
        Ball ball = objects.getBall();
        Point2D center = ball.getCenter();
        Vector2D dir = ball.getVelocity().normalized();
        double speed = ball.getSpeed() / 100;
        Point2D p0 = center.add(dir.multiply(ball.getRadius()));
        Point2D p1 = center.add(dir.multiply(speed * ball.getRadius()));
        painter[1].drawLine(p0, p1, Color.CYAN);
    }

}
