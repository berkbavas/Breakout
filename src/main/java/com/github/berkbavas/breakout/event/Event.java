package com.github.berkbavas.breakout.event;

import com.github.berkbavas.breakout.math.Point2D;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Event {
    private final EventType type;
    private final Point2D cursor;
}