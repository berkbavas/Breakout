package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.shapes.base.Disk;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ImageGenerator {

    private final Group container;
    private final Canvas gameBoard;

    private final Disk ball;

    public ImageGenerator(Disk ball) {
        this.ball = ball;

        container = new Group();
        gameBoard = new Canvas(Constants.GAME_BOARD_WIDTH, Constants.GAME_BOARD_HEIGHT);


        container.getChildren().add(gameBoard);
        gameBoard.setLayoutX(0);
        gameBoard.setLayoutY(0);
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


        gc.setFill(Color.WHITE);

        synchronized (ball.lock()) {
            float r = ball.getRadius();
            float left = ball.getLeft();
            float top = ball.getTop();
            gc.fillOval(left, top, 2 * r, 2 * r);
        }
    }

    public Group getContainer() {
        return container;
    }
}
