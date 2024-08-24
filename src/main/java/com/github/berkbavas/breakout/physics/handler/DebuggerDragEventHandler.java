package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;

import java.util.Set;

public class DebuggerDragEventHandler extends DragEventHandler {
    private final Set<Draggable> draggables;

    private Draggable target = null;
    private Event lastEvent;
    private Point2D delta;

    public DebuggerDragEventHandler(GameObjects objects) {
        super(objects);
        draggables = objects.getDraggables();
    }

    @Override
    public void listen(Event event) {
        if (event.getType() == EventType.MOUSE_PRESSED) {
            Draggable located = locateDraggable(event.getCursor());
            if (located != null) {
                target = located;
                lastEvent = event;
                delta = new Point2D(0, 0);
            }
        } else if (event.getType() == EventType.MOUSE_DRAGGED) {
            if (target != null) {
                Point2D current = event.getCursor();
                Point2D previous = lastEvent.getCursor();
                Point2D added = delta.add(current.subtract(previous));
                dragLater(target, added);
                delta = new Point2D(0,0);
                lastEvent = event;
            }
        } else if (event.getType() == EventType.MOUSE_RELEASED) {
            target = null;
        }
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
