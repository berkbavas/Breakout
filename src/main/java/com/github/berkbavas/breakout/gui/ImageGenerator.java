package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class ImageGenerator {

    @Getter
    private final Group container;
    private final Canvas gameBoard;

    private final GameObjects gameObjects;

    private final AnimationTimer timer;

    public ImageGenerator(GameObjects gameObjects) {
        this.gameObjects = gameObjects;

        container = new Group();
        gameBoard = new Canvas(Constants.GameBoard.WIDTH, Constants.GameBoard.HEIGHT);

        container.getChildren().add(gameBoard);
        gameBoard.setLayoutX(0);
        gameBoard.setLayoutY(0);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
    }

    public void update() {
        final double width = gameBoard.getWidth();
        final double height = gameBoard.getHeight();

        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        // Clear
        gc.clearRect(0, 0, width, height);


        // Background
        gc.setFill(Color.rgb(15, 15, 30));
        gc.fillRect(0, 0, width, height);

        // Ball
        Ball ball = gameObjects.getBall();
        paint(ball);

        // Paddle
        Paddle paddle = gameObjects.getPaddle();
        paint(paddle);
    }

    private void paint(Ball ball) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.save();
        gc.setFill(Constants.Ball.COLOR);
        double radius = ball.getRadius();
        double left = ball.getCenter().getX() - radius;
        double top = ball.getCenter().getY() - radius;
        gc.fillOval(left, top, 2 * radius, 2 * radius);
        gc.restore();
    }

    private void paint(Paddle paddle) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.save();
        gc.setFill(Constants.Paddle.COLOR);

        double x = paddle.getLeft().getP().getX();
        double y = paddle.getLeft().getP().getY();
        double w = paddle.getWidth();
        double h = paddle.getHeight();
        gc.fillRect(x, y, w, h);
        gc.restore();
    }


    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
