package com.github.berkbavas.breakout.core;

import lombok.Getter;

@Getter
public abstract class Manager {
    private boolean paused = false;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
}
