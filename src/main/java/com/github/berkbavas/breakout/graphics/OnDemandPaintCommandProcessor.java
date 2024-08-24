package com.github.berkbavas.breakout.graphics;

import java.util.HashMap;

public final class OnDemandPaintCommandProcessor {
    private static OnDemandPaintCommandProcessorInner IMPL;

    private OnDemandPaintCommandProcessor() {
    }

    public static void initialize(PaintCommandProcessor processor) {
        IMPL = new OnDemandPaintCommandProcessorInner(processor);
    }

    public static PaintCommandHandler getPaintCommandHandler(Object caller) {
        return IMPL.getPaintCommandHandler(caller);
    }

    private static class OnDemandPaintCommandProcessorInner {
        private final PaintCommandProcessor processor;

        private final HashMap<Object, PaintCommandHandler> handlers = new HashMap<>();

        OnDemandPaintCommandProcessorInner(PaintCommandProcessor processor) {
            this.processor = processor;
        }

        PaintCommandHandler getPaintCommandHandler(Object caller) {
            if (handlers.containsKey(caller)) {
                return handlers.get(caller);
            }

            PaintCommandHandler handler = processor.createHandler();
            handlers.put(caller, handler);
            return handler;
        }
    }

}
