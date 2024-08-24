package com.github.berkbavas.breakout.event;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

public interface EventListener {

    default void listen(Event event) {
    }

    void listen(MouseEvent event);
}
