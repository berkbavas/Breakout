package com.github.berkbavas.breakout.math;

public final class Util {
    public static final double EPSILON = 1e-6;

    private Util() {
    }

    public static <T extends Number> boolean fuzzyCompare(T a, T b) {
        return Math.abs(a.doubleValue() - b.doubleValue()) < Util.EPSILON;
    }

    public static <T extends Number> boolean isFuzzyZero(T a) {
        return fuzzyCompare(a, 0.0);
    }

    public static <T extends Number> boolean isGreaterThanOrEqualToZero(T a) {
        return -Util.EPSILON < a.doubleValue();
    }

    public static <T extends Number> boolean isLessThanOrEqualToZero(T a) {
        return a.doubleValue() < Util.EPSILON;
    }

    public static <T extends Number> boolean isFuzzyBetween(T lower, T value, T upper) {
        final double lw = lower.doubleValue();
        final double up = upper.doubleValue();

        if (lw > up) {
            return isFuzzyBetween(upper, value, lower);
        }

        final double val = value.doubleValue();
        return lw - EPSILON < val && val < up + EPSILON;
    }
}
