package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Draggable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Event {
    // Type of this event
    private final EventType type;

    // Node that is target to this particular event
    private final Draggable target;

    // World position of the mouse responsible for this particular event.
    private final Point2D position;

    // Difference in position since last call to query() method of EventDispatcher.
    private final Point2D delta;

    public Event() {
        type = EventType.UNDEFINED;
        target = null;
        position = null;
        delta = null;
    }
}