package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class GameObjects {
    private final World world;
    @Setter
    private Ball ball;
    @Setter
    private Paddle paddle;
}
