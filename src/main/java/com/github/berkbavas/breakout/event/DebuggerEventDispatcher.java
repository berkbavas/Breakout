package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;

import java.util.Optional;
import java.util.Set;

public class DebuggerEventDispatcher extends EventDispatcher {
    private final Set<Draggable> draggables;
    private Draggable target;

    public DebuggerEventDispatcher(Set<Draggable> draggables) {
        this.draggables = draggables;
        this.target = null;
    }

    @Override
    protected Optional<Draggable> shouldDispatch(EventType type, Point2D position) {
        switch (type) {
            case MOUSE_MOVED:
                return Optional.empty();
            case MOUSE_PRESSED: {
                target = locateDraggable(position);
                return Optional.ofNullable(target);
            }
            case MOUSE_DRAGGED: {
                if (target != null) {
                    return Optional.of(target);
                }
            }
            case MOUSE_RELEASED: {
                if (target != null) {
                    Draggable temp = target;
                    target = null;
                    return Optional.of(temp);
                }
            }
        }

        return Optional.empty();
    }

    private Draggable locateDraggable(Point2D query) {
        for (Draggable draggable : draggables) {
            if (draggable.contains(query)) {
                return draggable;
            }
        }
        return null;
    }
}
