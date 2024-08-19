package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
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
