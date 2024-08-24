package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import javafx.scene.paint.Color;

public class ThrowEventHandler implements EventListener {
    private final Ball ball;
    private final PaintCommandHandler painter;
    private final boolean isDebugMode;
    private boolean isPressedOnBall = false;
    private Point2D cursorPosition = new Point2D(0, 0);

    public ThrowEventHandler(GameObjects objects, boolean isDebugMode) {
        this.painter = OnDemandPaintCommandProcessor.getPaintCommandHandler(this);
        this.ball = objects.getBall();
        this.isDebugMode = isDebugMode;
    }

    public void update() {
        if (isPressedOnBall) {
            painter.clear();
            painter.drawLine(ball.getCenter(), cursorPosition, Color.YELLOW, 1);
        }
    }

    private void throwBall(Point2D cursorPosition) {
        Point2D center = ball.getCenter();
        Vector2D velocity = cursorPosition.subtract(center);
        ball.setVelocity(velocity);
    }

    @Override
    public void listen(Event event) {
        if (!isDebugMode) {
            return;
        }

        if (isPressedOnBall) {
            cursorPosition = event.getCursor();
        }

        if (event.getType() == EventType.MOUSE_PRESSED) {
            if (ball.contains(event.getCursor())) {
                cursorPosition = event.getCursor();
                isPressedOnBall = true;
            }
        } else if (event.getType() == EventType.MOUSE_RELEASED) {
            if (isPressedOnBall) {
                throwBall(cursorPosition);
            }
            isPressedOnBall = false;
            painter.clear();
        }
    }
}
