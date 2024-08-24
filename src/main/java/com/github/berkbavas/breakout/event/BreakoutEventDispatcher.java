package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;
import com.github.berkbavas.breakout.physics.node.Paddle;

import java.util.Optional;

public class BreakoutEventDispatcher extends EventDispatcher {
    private final Paddle paddle;
    private boolean focused;

    public BreakoutEventDispatcher(Paddle paddle) {
        this.paddle = paddle;
        this.focused = false;
    }

    @Override
    protected Optional<Draggable> shouldDispatch(EventType type, Point2D position) {
        if (type == EventType.MOUSE_CLICKED) {
            focused = !focused;

            if (!focused) {
                // We lost the focus, clear previously recorded events in the table.
                reset();
            }
        }

        return focused ? Optional.of(paddle) : Optional.empty();
    }
}
