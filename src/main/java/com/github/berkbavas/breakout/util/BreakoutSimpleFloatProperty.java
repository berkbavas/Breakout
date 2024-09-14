package com.github.berkbavas.breakout.util;

import javafx.beans.property.SimpleFloatProperty;

public class BreakoutSimpleFloatProperty extends SimpleFloatProperty {
    private final float[] data = new float[1];

    public BreakoutSimpleFloatProperty(float initialValue) {
        super(initialValue);
        data[0] = initialValue;

        addListener((o, newValue, oldValue) -> data[0] = newValue.floatValue());
    }

    public float[] getAsArray() {
        return data;
    }

    public void update() {
        set(data[0]);
    }
}
