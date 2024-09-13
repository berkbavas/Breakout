package com.github.berkbavas.breakout.core;

import com.github.berkbavas.breakout.physics.node.*;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Draggable;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GameObjects {
    private final World world;
    private final Set<Brick> bricks;
    private final Set<Obstacle> obstacles;
    private final Ball ball;
    private final Paddle paddle;

    private final Set<Collider> colliders;
    private final Set<Draggable> draggables;

    public GameObjects(World world, Set<Brick> bricks, Set<Obstacle> obstacles, Ball ball, Paddle paddle) {
        this.world = world;
        this.bricks = bricks;
        this.obstacles = obstacles;
        this.ball = ball;
        this.paddle = paddle;

        colliders = new HashSet<>();
        draggables = new HashSet<>();

        colliders.add(world);
        colliders.addAll(bricks);
        colliders.addAll(obstacles);
        colliders.add(paddle);

        draggables.addAll(obstacles);
        draggables.add(paddle);
    }
}
