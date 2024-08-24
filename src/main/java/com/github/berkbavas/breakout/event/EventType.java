package com.github.berkbavas.breakout.event;

import lombok.ToString;

@ToString
public enum EventType {
    MOUSE_MOVED,
    MOUSE_PRESSED,
    MOUSE_DRAGGED,
    MOUSE_RELEASED,
    MOUSE_CLICKED,
    UNDEFINED
}
