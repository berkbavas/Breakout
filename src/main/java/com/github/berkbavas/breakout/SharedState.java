package com.github.berkbavas.breakout;


import com.github.berkbavas.breakout.engine.TickResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedState {
    private volatile TickResult tickResult;
}
