package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EventDispatcher {

    private static class EventRecord {
        Event event = null;
        boolean queried = false;
    }

    private final ConcurrentHashMap<EventListener, EventRecord> listeners = new ConcurrentHashMap<>();

    public void receiveEvent(EventType type, Point2D position) {
        shouldDispatch(type, position).ifPresent(target -> dispatch(type, target, position));
    }

    public void addEventListener(EventListener listener) {
        listeners.put(listener, new EventRecord());
    }

    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    private void dispatch(EventType type, Draggable target, Point2D position) {

        listeners.forEach((listener, record) -> {
            Event event = record.event;
            Point2D delta;

            if (event == null) {
                // This is either the initial event or target has changed.
                // In both cases we set delta as (0, 0).
                delta = new Point2D(0, 0);
            } else {
                Point2D previousPosition = event.getPosition();
                Point2D currentDelta = position.subtract(previousPosition).toPoint2D();

                if (record.queried) {
                    delta = currentDelta;
                } else {
                    delta = event.getDelta().add(currentDelta);
                }
            }

            record.event = new Event(type, target, position, delta);
            record.queried = false;

            // We can dispatch the event here.
            listener.listen(record.event);
        });

    }

    public Optional<Event> query(EventListener listener) {
        EventRecord record = listeners.get(listener);

        if (record.event == null) {
            // No event occurred yet. Return empty optional.
            return Optional.empty();
        }

        if (record.queried) {
            // No new event since last query. Return empty optional.
            return Optional.empty();
        }

        record.queried = true;
        return Optional.of(record.event);
    }

    protected void reset() {
        listeners.forEach((listener, record) -> {
            record.event = null;
            record.queried = false;
        });

    }

    protected abstract Optional<Draggable> shouldDispatch(EventType type, Point2D position);
}
