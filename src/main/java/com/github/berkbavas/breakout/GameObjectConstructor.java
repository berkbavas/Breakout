package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.World;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.util.RandomGenerator;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public final class GameObjectConstructor {

    private GameObjectConstructor() {
    }

    public static GameObjects construct() {
        World world = constructWorld(Constants.World.WIDTH, Constants.World.HEIGHT);
        Ball ball = constructBall(Constants.Ball.INITIAL_X, Constants.Ball.INITIAL_Y, Constants.Ball.RADIUS, Constants.Ball.MIN_SPEED, Constants.Ball.MAX_SPEED);
        Paddle paddle = constructPaddle(Constants.Paddle.INITIAL_X, Constants.Paddle.INITIAL_Y, Constants.Paddle.WIDTH, Constants.Paddle.HEIGHT, Constants.Paddle.COLOR);
        ArrayList<Brick> bricks = constructBricks(6, 8);

        return new GameObjects(world, bricks, ball, paddle);
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

    private static ArrayList<Brick> constructBricks(int rows, int columns) {
        ArrayList<Brick> bricks = new ArrayList<>();
        double totalWidth = columns * (Constants.Brick.WIDTH + Constants.Brick.HORIZONTAL_SPACING) - Constants.Brick.HORIZONTAL_SPACING;
        double left = 0.5 * (Constants.World.WIDTH - totalWidth);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final double x = j * (Constants.Brick.WIDTH + Constants.Brick.HORIZONTAL_SPACING) + left;
                final double y = i * (Constants.Brick.HEIGHT + Constants.Brick.VERTICAL_SPACING) + Constants.World.TOP_PADDING;

                bricks.add(new Brick(x, y, Constants.Brick.WIDTH, Constants.Brick.HEIGHT, Constants.Brick.COLOR));
            }
        }

        return bricks;
    }

}
