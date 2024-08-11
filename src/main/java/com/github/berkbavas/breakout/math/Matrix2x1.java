package com.github.berkbavas.breakout.math;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Matrix2x1 {
    private final double m00;
    private final double m10;

    public Matrix2x1(double m00, double m10) {
        this.m00 = m00;
        this.m10 = m10;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Matrix2x1) {
            final Matrix2x1 other = (Matrix2x1) object;
            return Util.fuzzyCompare(m00, other.m00) && Util.fuzzyCompare(m10, other.m10);
        } else {
            return false;
        }
    }
}
