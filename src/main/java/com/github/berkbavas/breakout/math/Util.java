package com.github.berkbavas.breakout.math;

public final class Util {
    public static final double EPSILON = 1e-6;

    private Util() {
    }

    public static <T extends Number> boolean fuzzyCompare(T a, T b) {
        final double x = a.doubleValue();
        final double y = b.doubleValue();

        if (Double.isNaN(x) && Double.isNaN(y)) {
            return true;
        }

        return Math.abs(x - y) < Util.EPSILON;
    }

    public static <T extends Number> boolean isFuzzyZero(T a) {
        return fuzzyCompare(a, 0.0);
    }

    public static <T extends Number> boolean isBetween(T lower, T value, T upper) {
        final double lw = lower.doubleValue();
        final double up = upper.doubleValue();

        if (lw > up) {
            return isBetween(upper, value, lower);
        }

        final double val = value.doubleValue();
        return lw <= val && val <= up;
    }

    public static double clamp(double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }

}
