package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.Room;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;

import java.security.SecureRandom;

public final class GameObjectConstructor {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private GameObjectConstructor() {
    }


    public static GameObjects construct() {

        Room room = constructRoom(Constants.GameBoard.WIDTH, Constants.GameBoard.HEIGHT);
        Ball ball = constructBall(Constants.Ball.INITIAL_X, Constants.Ball.INITIAL_Y, Constants.Ball.RADIUS, Constants.Ball.MIN_SPEED, Constants.Ball.MAX_SPEED);
        Paddle paddle = constructPaddle(Constants.Paddle.INITIAL_X, Constants.Paddle.INITIAL_Y, Constants.Paddle.WIDTH, Constants.Paddle.HEIGHT, Constants.Paddle.COLOR);

        return new GameObjects(room, ball, paddle);
    }

    private static Paddle constructPaddle(float x, float y, float width, float height, Color color) {
        return new Paddle(x, y, width, height, color);

    }

    private static Ball constructBall(float cx, float cy, float radius, float minSpeed, float maxSpeed) {
        Point2D center = new Point2D(cx, cy);
        float speed = nextFloat(minSpeed, maxSpeed);
        Vector2D velocity = generateRandomVelocity(speed);
        return new Ball(center, radius, velocity);
    }

    private static Room constructRoom(float width, float height) {
        return new Room(0, 0, width, height);
    }

    public static Vector2D generateRandomVelocity(float speed) {
        final float vx = nextFloat();
        final float vy = nextFloat();
        Vector2D direction = new Vector2D(vx, vy).normalized();

        return direction.multiply(speed);
    }

    public static float nextFloat() {
        return SECURE_RANDOM.nextFloat();
    }

    public static float nextFloat(float max) {
        return max * SECURE_RANDOM.nextFloat();
    }

    public static float nextFloat(float min, float max) {
        return min + (max - min) * SECURE_RANDOM.nextFloat();
    }
}
