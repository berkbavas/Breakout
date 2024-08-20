package com.github.berkbavas.breakout.util;

import com.github.berkbavas.breakout.math.Vector2D;

import java.security.SecureRandom;

public final class RandomGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomGenerator() {
    }

    public static Vector2D generateRandomVelocity(double speed) {
        final double vx = nextDouble(0.5, 1.0);
        final double vy = nextDouble(0.5, 1.0);
        Vector2D direction = new Vector2D(vx, vy).normalized();

        return direction.multiply(speed);
    }

    public static double nextDouble() {
        return SECURE_RANDOM.nextDouble();
    }

    public static double nextDouble(double max) {
        return max * SECURE_RANDOM.nextDouble();
    }

    public static double nextDouble(double min, double max) {
        return min + (max - min) * SECURE_RANDOM.nextDouble();
    }
}
