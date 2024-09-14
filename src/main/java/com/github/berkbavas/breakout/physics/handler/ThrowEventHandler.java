package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.core.GameObjects;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.gui.GraphicsEngine;
import com.github.berkbavas.breakout.gui.PaintCommandHandler;
import com.github.berkbavas.breakout.gui.TrajectoryPlotter;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class ThrowEventHandler implements EventListener {
    public static SimpleBooleanProperty PLOT_TRAJECTORY_ENABLED = new SimpleBooleanProperty(false);
    private final PaintCommandHandler painter;
    private final Ball ball;
    private final Set<Collider> colliders;
    private Point2D cursorPosition = new Point2D(0, 0);
    @Setter
    @Getter
    private boolean isEnabled = true;

    public ThrowEventHandler(GameObjects objects) {
        this.painter = GraphicsEngine.createHandler();
        this.ball = objects.getBall();
        this.colliders = objects.getColliders();

        PLOT_TRAJECTORY_ENABLED.addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                TrajectoryPlotter.show(this);
            } else {
                TrajectoryPlotter.hide(this);
            }
        }));
    }

    public void update() {
        if (ball.isFreeze()) {
            painter.clear();
            painter.drawLine(ball.getCenter(), cursorPosition, Color.YELLOW, 2);
            Vector2D velocity = calculateVelocity(cursorPosition);
            plotTrajectory(velocity);
        }

        if (!PLOT_TRAJECTORY_ENABLED.get()) {
            TrajectoryPlotter.hide(this);
        }
    }

    @Override
    public void listen(MouseEvent event) {
        if (!isEnabled) {
            return;
        }

        updateCursorPositionIfApplicable(event);

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            freezeBallIfApplicable(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            throwBallIfApplicable(event);
            painter.clear();
        }
    }

    private void updateCursorPositionIfApplicable(MouseEvent event) {
        if (ball.isFreeze()) {
            cursorPosition = TransformationHelper.fromCanvasToWorld(event.getX(), event.getY());
            event.consume();
        }
    }

    private void freezeBallIfApplicable(MouseEvent event) {
        Point2D worldPos = TransformationHelper.fromCanvasToWorld(event.getX(), event.getY());

        if (ball.contains(worldPos, 4)) {
            cursorPosition = worldPos;
            ball.setFreeze(true);
            event.consume();
        }
    }

    private void throwBallIfApplicable(MouseEvent event) {
        if (ball.isFreeze()) {
            Vector2D velocity = calculateVelocity(cursorPosition);
            ball.setFreeze(false);
            ball.setVelocity(velocity);
            plotTrajectory(velocity);
            event.consume();
        }
    }

    private Vector2D calculateVelocity(Point2D cursorPosition) {
        return cursorPosition.subtract(ball.getCenter()).multiply(5);
    }

    public void plotTrajectory(Vector2D velocity) {
        TrajectoryPlotter.plot(this, colliders, ball.getCenter(), ball.getRadius(), velocity);
    }
}
