package com.github.berkbavas.breakout.gui;

public final class OnDemandPaintCommandProcessor {
    private static PaintCommandProcessor PROC;

    private OnDemandPaintCommandProcessor() {
    }

    public static void initialize(PaintCommandProcessor processor) {
        PROC = processor;
    }

    public static PaintCommandHandler getNextPaintCommandHandler() {
        return PROC.createHandler();
    }

}