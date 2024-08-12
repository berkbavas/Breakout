package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.World;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.util.RandomGenerator;
import javafx.scene.paint.Color;

public final class GameObjectConstructor {

    private GameObjectConstructor() {
    }

    public static GameObjects construct() {

        World room = constructWorld(Constants.GameBoard.WIDTH, Constants.GameBoard.HEIGHT);
        Ball ball = constructBall(Constants.Ball.INITIAL_X, Constants.Ball.INITIAL_Y, Constants.Ball.RADIUS, Constants.Ball.MIN_SPEED, Constants.Ball.MAX_SPEED);
        Paddle paddle = constructPaddle(Constants.Paddle.INITIAL_X, Constants.Paddle.INITIAL_Y, Constants.Paddle.WIDTH, Constants.Paddle.HEIGHT, Constants.Paddle.COLOR);

        return new GameObjects(room, ball, paddle);
    }

    private static Paddle constructPaddle(double x, double y, double width, double height, Color color) {
        return new Paddle(x, y, width, height, color);
    }

    private static Ball constructBall(double cx, double cy, double radius, double minSpeed, double maxSpeed) {
        Point2D center = new Point2D(cx, cy);
        double speed = RandomGenerator.nextDouble(minSpeed, maxSpeed);
        Vector2D velocity = RandomGenerator.generateRandomVelocity(speed);
        return new Ball(center, radius, velocity);
    }

    private static World constructWorld(double width, double height) {
        return new World(0, 0, width, height);
    }

}
