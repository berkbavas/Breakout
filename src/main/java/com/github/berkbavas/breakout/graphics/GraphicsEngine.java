package com.github.berkbavas.breakout.graphics;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.Obstacle;
import com.github.berkbavas.breakout.physics.node.Paddle;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

public class GraphicsEngine implements EventListener, PaintCommandProcessor {
    @Getter
    private final Group container;
    @Getter
    private final Canvas canvas;
    private final GameObjects objects;
    private final Painter painter;
    private final ArrayList<PaintCommandHandler> handlers = new ArrayList<>();

    @Getter
    private final double width;
    @Getter
    private final double height;

    public GraphicsEngine(GameObjects objects) {
        this.objects = objects;

        this.width = objects.getWorld().getWidth();
        this.height = objects.getWorld().getHeight();

        canvas = new Canvas(width, height);
        container = new Group();
        container.getChildren().add(canvas);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        GraphicsContext context = canvas.getGraphicsContext2D();
        painter = new Painter(context, width, height);
    }

    public void update() {
        painter.save();

        painter.clear();

        // Background
        painter.fillBackground(objects.getWorld().getColor());


        // Ball
        Ball ball = objects.getBall();
        painter.fillCircle(ball);


        // Paddle
        Paddle paddle = objects.getPaddle();
        painter.fillRoundRectangle(paddle, Constants.Paddle.ARC_RADIUS, Constants.Paddle.ARC_RADIUS);


        // Bricks
        Set<Brick> bricks = objects.getBricks();

        for (Brick brick : bricks) {
            if (brick.isHit()) {
                continue;
            }

            painter.fillRoundRectangle(brick, Constants.Brick.ARC_RADIUS, Constants.Brick.ARC_RADIUS);
        }


        // Obstacles
        Set<Obstacle> obstacles = objects.getObstacles();

        for (Obstacle obstacle : obstacles) {
            painter.fillPolygon(obstacle);
        }


        // Process commands here
        for (PaintCommandHandler handler : handlers) {
            painter.processCommands(handler);
        }

        painter.restore();
    }

    @Override
    public void listen(Event event) {

    }

    @Override
    public PaintCommandHandler createHandler() {
        PaintCommandHandler handler = new PaintCommandHandler();
        handlers.add(handler);
        return handler;
    }
}