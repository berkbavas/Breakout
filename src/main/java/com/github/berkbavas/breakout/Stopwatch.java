package com.github.berkbavas.breakout;

public class Stopwatch {
    private long start;

    public double elapsed() {
        return (System.currentTimeMillis() - start) / 1000.0;
    }

    public void reset() {
        start = System.currentTimeMillis();
    }
}
