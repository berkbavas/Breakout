package com.github.berkbavas.breakout.event;

import javafx.event.Event;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class EventDispatcher {

    private final ArrayList<EventListener> listeners = new ArrayList<>();

    public void receiveEvent(Event event) {
        if (event instanceof MouseEvent) {
            dispatch((MouseEvent) event);
        } else {
            dispatch(event);
        }
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    private void dispatch(Event event) {
        listeners.forEach((listener -> listener.listen(event)));
    }

    private void dispatch(MouseEvent event) {
        listeners.forEach((listener -> listener.listen(event)));
    }

}
