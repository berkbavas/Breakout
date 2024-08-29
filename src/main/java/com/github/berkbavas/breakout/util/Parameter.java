package com.github.berkbavas.breakout.util;

import com.github.berkbavas.breakout.math.Util;
import lombok.Getter;

@Getter
public class Parameter {
    private final double minimum;
    private final double maximum;
    private double value;

    public Parameter(double minimum, double initial, double maximum) {
        this.minimum = minimum;
        this.value = initial;
        this.maximum = maximum;
    }

    public Parameter(double initial) {
        this(0.0, initial, 1.0);
    }

    public void setValue(double newValue) {
        this.value = Util.clamp(minimum, newValue, maximum);
    }
}
