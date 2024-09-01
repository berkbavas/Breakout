package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import com.github.berkbavas.breakout.physics.node.base.Drawable;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Set;

public class DebuggerDragEventHandler extends DragEventHandler {
    private final Set<Draggable> draggables;
    private final PaintCommandHandler painter;

    private Draggable target = null;
    private MouseEvent lastEvent;
    private Point2D delta;

    public DebuggerDragEventHandler(GameObjects objects) {
        super(objects);
        draggables = objects.getDraggables();
        painter = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
    }

    @Override
    public void listen(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            Point2D worldPos = TransformationHelper.fromSceneToWorld(event.getSceneX(), event.getSceneY());
            Draggable located = locateDraggable(worldPos);
            if (located != null) {
                target = located;
                acceptIfDrawable(target, true);
                paintIfDrawable(target);

                lastEvent = event;
                delta = new Point2D(0, 0);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (target != null) {
                Point2D current = TransformationHelper.fromSceneToWorld(event.getSceneX(), event.getSceneY());
                Point2D previous = TransformationHelper.fromSceneToWorld(lastEvent.getSceneX(), lastEvent.getSceneY());
                Point2D added = delta.add(current.subtract(previous));

                translate(target, added);
                paintIfDrawable(target);

                lastEvent = event;
                delta = new Point2D(0, 0);
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (target != null) {
                acceptIfDrawable(target, false);
            }

            target = null;
        }
    }

    private void acceptIfDrawable(Draggable target, boolean accept) {
        if (target instanceof Drawable) {
            Drawable drawable = (Drawable) target;
            // I will render this node from now on if accept == true.
            drawable.setIsActiveDrawable(!accept);
            painter.clear();
        }
    }

    private void paintIfDrawable(Draggable target) {
        if (target instanceof Drawable) {
            Drawable drawable = (Drawable) target;
            painter.clear();
            painter.fill(drawable, Color.rgb(255, 255, 255, 0.6));
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