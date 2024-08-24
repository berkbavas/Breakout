package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import javafx.scene.paint.Color;

public class ThrowEventHandler implements EventListener {
    private final GameObjects objects;
    private final PaintCommandHandler painter;

    private boolean isPressedOnBall = false;
    private boolean throwBall = false;
    private Point2D cursorPosition = new Point2D(0, 0);

    public ThrowEventHandler(GameObjects objects) {
        this.objects = objects;
        this.painter = OnDemandPaintCommandProcessor.getPaintCommandHandler(this);
    }

    public void update() {

        if (isPressedOnBall) {
            painter.clear();
            painter.drawLine(objects.getBall().getCenter(), cursorPosition, Color.YELLOW, 1);
        }

        if (throwBall) {
            throwBall();
            throwBall = false;
        }
    }

    private void throwBall() {
        System.out.println("throwBall");
    }

    @Override
    public void listen(Event event) {
        if (event.getTarget() == objects.getBall()) {
            cursorPosition = event.getPosition();

            if (event.getType() == EventType.MOUSE_PRESSED) {
                isPressedOnBall = true;
            } else if (event.getType() == EventType.MOUSE_RELEASED) {
                if (isPressedOnBall) {
                    throwBall = true;
                }

                isPressedOnBall = false;
                painter.clear();
            }
        }
    }
}
