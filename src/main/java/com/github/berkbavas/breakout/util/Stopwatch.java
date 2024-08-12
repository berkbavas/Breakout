package com.github.berkbavas.breakout.util;

public class Stopwatch {

    private final static long SECONDS_TO_NANOSECONDS = 1_000_000_000;
    private final static long MILLISECONDS_TO_NANOSECONDS = 1_000_000;
    private final static long MICROSECONDS_TO_NANOSECONDS = 1_000;
    private final static double NANOSECONDS_TO_SECONDS = 1.0 / SECONDS_TO_NANOSECONDS;
    private final static double NANOSECONDS_TO_MILLISECONDS = 1.0 / MILLISECONDS_TO_NANOSECONDS;
    private final static double NANOSECONDS_TO_MICROSECONDS = 1.0 / MICROSECONDS_TO_NANOSECONDS;

    private long start = System.nanoTime();

    public void start() {
        start = System.nanoTime();
    }

    public void restart() {
        start = System.nanoTime();
    }

    public void reset() {
        start = System.nanoTime();
    }

    public double getSeconds() {
        return getNanoseconds() * NANOSECONDS_TO_SECONDS;
    }

    public long getMilliseconds() {
        final double elapsed = getNanoseconds();
        return (long) (elapsed * NANOSECONDS_TO_MILLISECONDS);
    }

    public long getMicroseconds() {
        final double elapsed = getNanoseconds();
        return (long) (elapsed * NANOSECONDS_TO_MICROSECONDS);
    }

    public long getNanoseconds() {
        final long current = System.nanoTime();
        return current - start;
    }

}
