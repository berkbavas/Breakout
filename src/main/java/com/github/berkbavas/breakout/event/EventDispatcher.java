package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;

import java.util.ArrayList;

public class EventDispatcher {

    private final ArrayList<EventListener> listeners = new ArrayList<>();

    public void receiveEvent(EventType type, Point2D position) {
        Event event = new Event(type, position);
        dispatch(event);
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

}
