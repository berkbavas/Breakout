package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class GameObjects {
    private Room room;
    private Ball ball;
    private Paddle paddle;
}
