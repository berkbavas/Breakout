package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class GameObjects {
    private final World world;
    private final ArrayList<Brick> bricks;

    @Setter
    private volatile Ball ball;
    @Setter
    private volatile Paddle paddle;

}
