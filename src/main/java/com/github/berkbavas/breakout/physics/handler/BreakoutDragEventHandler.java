package com.github.berkbavas.breakout.physics.handler;

import com.github.berkbavas.breakout.core.GameObjects;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Paddle;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.event.EventTarget;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;

public class BreakoutDragEventHandler extends DragEventHandler {
    private final Paddle paddle;
    private final Robot robot = new Robot();
    private MouseEvent lastEvent;
    private Point2D delta;
    private boolean focused = false;
    private boolean ignoreMouseMove = false;

    public BreakoutDragEventHandler(GameObjects objects) {
        super(objects);
        this.paddle = objects.getPaddle();
    }

    @Override
    public void listen(MouseEvent event) {
        if (ignoreMouseMove) {
            ignoreMouseMove = false;
            lastEvent = event;
            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            focused = !focused;

            if (focused) {
                lastEvent = event;
                delta = new Point2D(0, 0);
            }

            updateCursor(event);

        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED || event.getEventType() == MouseEvent.MOUSE_MOVED) {
            if (focused) {
                Point2D current = TransformationHelper.fromCanvasToWorld(event.getX(), event.getY());
                Point2D previous = TransformationHelper.fromCanvasToWorld(lastEvent.getX(), lastEvent.getY());
                Point2D added = delta.add(current.subtract(previous));

                // Move cursor to the previous position
                ignoreMouseMove = true;
                moveMouse(lastEvent);

                lastEvent = event;
                delta = new Point2D(0, 0);

                translate(paddle, added);
            }
        }
    }

    private void updateCursor(MouseEvent event) {
        EventTarget target = event.getTarget();
        if (target instanceof Node) {
            Node node = (Node) target;
            node.getScene().setCursor(focused ? Cursor.NONE : Cursor.DEFAULT);
        }
    }

    private void moveMouse(MouseEvent event) {
        EventTarget target = event.getTarget();
        if (target instanceof Node) {
            Node node = (Node) target;
            Point2D sceneCenter = TransformationHelper.getCanvasCenter();
            javafx.geometry.Point2D screen = node.localToScreen(new javafx.geometry.Point2D(sceneCenter.getX(), sceneCenter.getY()));

            if (screen != null) {
                robot.mouseMove(screen.getX(), screen.getY());
            } else {
                robot.mouseMove(event.getScreenX(), event.getScreenY());
            }
        } else {
            robot.mouseMove(event.getScreenX(), event.getScreenY());
        }
    }
}
