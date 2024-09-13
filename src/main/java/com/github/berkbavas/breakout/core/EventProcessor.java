package com.github.berkbavas.breakout.core;

import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.physics.handler.BreakoutDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DebuggerDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DragEventHandler;
import com.github.berkbavas.breakout.physics.handler.ThrowEventHandler;

public class EventProcessor {
    private final DragEventHandler dragEventHandler;
    private final ThrowEventHandler throwEventHandler;

    public EventProcessor(GameObjects objects, EventDispatcher dispatcher, boolean isDebugMode) {
        this.throwEventHandler = new ThrowEventHandler(objects);
        this.throwEventHandler.setEnabled(isDebugMode);

        if (isDebugMode) {
            dragEventHandler = new DebuggerDragEventHandler(objects);
        } else {
            dragEventHandler = new BreakoutDragEventHandler(objects);
        }

        dispatcher.addEventListener(throwEventHandler);
        dispatcher.addEventListener(dragEventHandler);
    }

    public void update() {
        throwEventHandler.update();
        dragEventHandler.update();
    }
}
