package com.github.berkbavas.breakout;

public class Stopwatch {
    private final static long SECONDS_TO_NANOSECONDS = 1_000_000_000;
    private final static float NANOSECONDS_TO_SECONDS = 1.0f / SECONDS_TO_NANOSECONDS;

    private long start = System.nanoTime();

    // Returns elapsed time in seconds.
    public float elapsed() {
        final long current = System.nanoTime();
        return (current - start) * NANOSECONDS_TO_SECONDS;
    }

    // Returns elapsed time in seconds and resets the chronometer.
    public float restart() {
        final float elapsed = elapsed();
        reset();
        return elapsed;
    }

    public void reset() {
        start = System.nanoTime();
    }
}
