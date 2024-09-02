package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.TrajectoryPlotter;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

public class ThrowEventHandler implements EventListener {
    private final PaintCommandHandler painter;
    private final Ball ball;
    private final TrajectoryPlotter plotter;
    private boolean isPressedOnBall = false;
    private Point2D cursorPosition = new Point2D(0, 0);

    @Setter
    @Getter
    private boolean isEnabled = true;

    public ThrowEventHandler(GameObjects objects) {
        this.painter = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
        this.ball = objects.getBall();
        this.plotter = new TrajectoryPlotter(objects.getWorld(), objects.getColliders(), objects.getBall());
    }

    public void update() {
        if (isPressedOnBall) {
            ball.makeSteady();
            painter.clear();
            painter.drawLine(ball.getCenter(), cursorPosition, Color.YELLOW, 2);
            Vector2D velocity = calculateVelocity(cursorPosition);
            plotTrajectory(velocity);
        }
    }

    @Override
    public void listen(MouseEvent event) {
        if (!isEnabled) {
            return;
        }

        if (isPressedOnBall) {
            cursorPosition = TransformationHelper.fromSceneToWorld(event.getSceneX(), event.getSceneY());
        }

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            Point2D worldPos = TransformationHelper.fromSceneToWorld(event.getSceneX(), event.getSceneY());

            if (ball.contains(worldPos, 3)) {
                cursorPosition = worldPos;
                isPressedOnBall = true;
            }

        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (isPressedOnBall) {
                Vector2D velocity = calculateVelocity(cursorPosition);
                throwBall(velocity);
                plotTrajectory(velocity);
            }
            isPressedOnBall = false;
            painter.clear();
        }
    }

    private void plotTrajectory(Vector2D velocity) {
        Ball copy = ball.copy();
        copy.setVelocity(velocity);
        plotter.setBall(copy);
        plotter.plotTrajectory();
    }

    private void throwBall(Vector2D velocity) {
        ball.setVelocity(velocity);
    }

    private Vector2D calculateVelocity(Point2D cursorPosition) {
        Point2D center = ball.getCenter();
        Vector2D velocity = cursorPosition.subtract(center);
        return velocity.multiply(5);
    }
}
