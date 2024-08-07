package com.github.berkbavas.breakout.util;

public class Stopwatch {

    private final static long SECONDS_TO_NANOSECONDS = 1_000_000_000;
    private final static long MILLISECONDS_TO_NANOSECONDS = 1_000_000;
    private final static long MICROSECONDS_TO_NANOSECONDS = 1_000;
    private final static float NANOSECONDS_TO_SECONDS = 1.0f / SECONDS_TO_NANOSECONDS;
    private final static float NANOSECONDS_TO_MILLISECONDS = 1.0f / MILLISECONDS_TO_NANOSECONDS;
    private final static float NANOSECONDS_TO_MICROSECONDS = 1.0f / MICROSECONDS_TO_NANOSECONDS;

    private long start = System.nanoTime();

    public void start() {
        start = System.nanoTime();
    }

    // Returns elapsed time in seconds.
    public float elapsed() {
        return elapsedNanoSeconds() * NANOSECONDS_TO_SECONDS;
    }

    public long elapsedMilliSeconds() {
        final float elapsed = elapsedNanoSeconds();
        return (long) (elapsed * NANOSECONDS_TO_MILLISECONDS);
    }

    public long elapsedMicroSeconds() {
        final float elapsed = elapsedNanoSeconds();
        return (long) (elapsed * NANOSECONDS_TO_MICROSECONDS);
    }

    public long elapsedNanoSeconds() {
        final long current = System.nanoTime();
        return current - start;
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
