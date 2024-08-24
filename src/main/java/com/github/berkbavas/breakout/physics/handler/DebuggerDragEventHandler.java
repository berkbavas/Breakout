package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class DebuggerDragEventHandler extends DragEventHandler {
    private final Set<Draggable> draggables;

    private Draggable target = null;
    private MouseEvent lastEvent;
    private Point2D delta;

    public DebuggerDragEventHandler(GameObjects objects) {
        super(objects);
        draggables = objects.getDraggables();
    }

    @Override
    public void listen(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            Point2D worldPos = TransformationHelper.fromSceneToWorld(event.getX(), event.getY());
            Draggable located = locateDraggable(worldPos);
            if (located != null) {
                target = located;
                lastEvent = event;
                delta = new Point2D(0, 0);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (target != null) {
                Point2D current = TransformationHelper.fromSceneToWorld(event.getX(), event.getY());
                Point2D previous = TransformationHelper.fromSceneToWorld(lastEvent.getX(), lastEvent.getY());
                Point2D added = delta.add(current.subtract(previous));
                delta = new Point2D(0, 0);
                lastEvent = event;
                dragLater(target, added);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
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
