package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Paddle;

public class BreakoutDragEventHandler extends DragEventHandler {
    private final Paddle paddle;
    private Event lastEvent;
    private Point2D delta;
    private boolean focused = false;

    public BreakoutDragEventHandler(GameObjects objects) {
        super(objects);
        paddle = objects.getPaddle();
    }

    @Override
    public void listen(Event event) {
        if (event.getType() == EventType.MOUSE_CLICKED) {
            focused = !focused;
            if (focused) {
                delta = new Point2D(0, 0);
                lastEvent = event;
            }
        } else if (event.getType() == EventType.MOUSE_DRAGGED || event.getType() == EventType.MOUSE_MOVED) {
            if (focused) {
                Point2D current = event.getCursor();
                Point2D previous = lastEvent.getCursor();
                Point2D added = delta.add(current.subtract(previous));

                dragLater(paddle, added);

                lastEvent = event;
                delta = new Point2D(0, 0);
            }
        }
    }
}
