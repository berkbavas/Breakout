package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.SharedState;
import com.github.berkbavas.breakout.engine.Collision;
import com.github.berkbavas.breakout.engine.TickResult;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

public class ImageGenerator {

    @Getter
    private final Group container;
    @Getter
    private final Canvas gameBoard;

    private final GameObjects gameObjects;
    private final SharedState sharedState;

    public ImageGenerator(GameObjects gameObjects, SharedState sharedState) {
        this.gameObjects = gameObjects;
        this.sharedState = sharedState;

        container = new Group();
        gameBoard = new Canvas(Constants.World.WIDTH, Constants.World.HEIGHT);

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

        paintForDebug();

        // Ball
        Ball ball = gameObjects.getBall();
        paint(ball);

        // Paddle
        Paddle paddle = gameObjects.getPaddle();
        paint(paddle);

        ArrayList<Brick> bricks = gameObjects.getBricks();
        paint(bricks);
    }

    private void paintForDebug() {
        TickResult result = sharedState.getTickResult();

        if (result == null) {
            return;
        }

        // Line(s) between potential collision contacts
        Set<Collision> collisions = result.getCollisions();

        for (Collision collision : collisions) {
            Point2D p0 = collision.getContactPointOnBall();
            Point2D p1 = collision.getContactPointOnEdge();
            drawLine(p0, p1, Color.RED);
        }

        // Velocity indicator
        Ball ball = gameObjects.getBall();
        Point2D center = ball.getCenter();
        Vector2D dir = ball.getVelocity().normalized();
        Point2D p0 = center.add(dir.multiply(ball.getRadius()));
        Point2D p1 = center.add(dir.multiply(4 * ball.getRadius()));
        drawLine(p0, p1, Color.CYAN);
    }

    private void drawDashedLine(Point2D p0, Point2D p1, Color color, double... dashes) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.save();
        gc.setStroke(color);
        gc.setLineDashes(dashes);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
        gc.restore();
    }

    private void drawLine(Point2D p0, Point2D p1, Color color) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.save();
        gc.setStroke(color);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
        gc.restore();
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

    private void paint(ArrayList<Brick> bricks) {
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.save();

        for (Brick brick : bricks) {
            if (brick.isHit()) {
                continue;
            }

            gc.setFill(brick.getColor());
            final double x = brick.getLeftTop().getX();
            final double y = brick.getLeftTop().getY();
            final double w = brick.getWidth();
            final double h = brick.getHeight();
            gc.fillRect(x, y, w, h);
        }

        gc.restore();
    }
}
