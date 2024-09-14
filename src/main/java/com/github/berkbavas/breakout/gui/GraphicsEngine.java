package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.core.GameObjects;
import com.github.berkbavas.breakout.core.Manager;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.Obstacle;
import com.github.berkbavas.breakout.physics.node.Paddle;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

@Getter
public class GraphicsEngine extends Manager {
    private final static ArrayList<PaintCommandHandler> handlers = new ArrayList<>();

    private final StackPane root;
    private final Canvas canvas;
    private final GameObjects objects;
    private final Ball ball;
    private final Painter painter;
    private final VisualDebugger visualDebugger;
    private final double width;
    private final double height;
    private final boolean isDebugMode;

    public GraphicsEngine(GameObjects objects, boolean isDebugMode) {
        this.objects = objects;
        this.ball = objects.getBall();
        this.width = objects.getWorld().getWidth();
        this.height = objects.getWorld().getHeight();
        this.isDebugMode = isDebugMode;

        canvas = new Canvas(width, height);
        root = new StackPane();
        root.getChildren().add(canvas);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        GraphicsContext context = canvas.getGraphicsContext2D();
        painter = new Painter(context, width, height);

        visualDebugger = new VisualDebugger(ball, createHandler());
    }

    public void update(Tick<? extends Collision> result) {
        if (isPaused()) {
            return;
        }

        // If debug mode is on, paint the output of algorithm for visual debugging.
        if (isDebugMode) {
            visualDebugger.clear();
            visualDebugger.paint(result);
            visualDebugger.paint();
        }

        painter.save();

        painter.clear();

        // Background
        painter.fillBackground(objects.getWorld().getColor());

        // Ball
        Ball ball = objects.getBall();
        painter.fill(ball);

        // Paddle
        Paddle paddle = objects.getPaddle();
        if (paddle.isActiveDrawable()) {
            painter.fillRoundRectangle(paddle, Constants.Paddle.ARC_RADIUS, Constants.Paddle.ARC_RADIUS);
        }

        // Bricks
        Set<Brick> bricks = objects.getBricks();

        for (Brick brick : bricks) {
            if (brick.isActiveDrawable()) {
                painter.fillRoundRectangle(brick, Constants.Brick.ARC_RADIUS, Constants.Brick.ARC_RADIUS);
            }
        }

        // Obstacles
        Set<Obstacle> obstacles = objects.getObstacles();

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isActiveDrawable()) {
                painter.fill(obstacle);
            }
        }

        // Process commands here
        for (PaintCommandHandler handler : handlers) {
            painter.processCommands(handler);
        }

        painter.restore();

    }

    public static PaintCommandHandler createHandler() {
        PaintCommandHandler handler = new PaintCommandHandler();
        handlers.add(handler);
        return handler;
    }
}
