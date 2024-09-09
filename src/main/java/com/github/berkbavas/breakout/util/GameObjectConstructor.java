package com.github.berkbavas.breakout.util;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GameObjectConstructor {

    private GameObjectConstructor() {
    }

    public static GameObjects construct(boolean isDebugMode) {
        World world = constructWorld(Constants.World.WIDTH, Constants.World.HEIGHT);
        Ball ball = constructBall(Constants.Ball.INITIAL_X, Constants.Ball.INITIAL_Y, Constants.Ball.RADIUS, Constants.Ball.MIN_SPEED, Constants.Ball.MAX_SPEED);
        Paddle paddle = constructPaddle(Constants.Paddle.INITIAL_X, Constants.Paddle.INITIAL_Y, Constants.Paddle.WIDTH, Constants.Paddle.HEIGHT, Constants.Paddle.COLOR);
        Set<Brick> bricks;
        Set<Obstacle> obstacles;

        if (isDebugMode) {
            bricks = Set.of();
            paddle.setIsActiveDrawable(false);
            paddle.setActiveCollider(false);
            paddle.setActiveDraggable(false);
            obstacles = constructObstacles();
        } else {
            bricks = constructBricks(8, 12);
            obstacles = Set.of();
        }

        return new GameObjects(world, bricks, obstacles, ball, paddle);
    }

    private static Paddle constructPaddle(double x, double y, double width, double height, Color color) {
        return new Paddle(x, y, width, height, color);
    }

    private static Ball constructBall(double cx, double cy, double radius, double minSpeed, double maxSpeed) {
        Point2D center = new Point2D(cx, cy);
        double speed = RandomGenerator.nextDouble(minSpeed, maxSpeed);
        Vector2D velocity = RandomGenerator.generateRandomVelocity(speed);
        return new Ball(center, radius, velocity, Constants.Ball.COLOR);
    }

    private static World constructWorld(double width, double height) {
        return new World(0, 0, width, height, Constants.World.BACKGROUND_COLOR);
    }

    private static Set<Brick> constructBricks(int rows, int columns) {
        Set<Brick> bricks = new HashSet<>();
        double totalWidth = columns * (Constants.Brick.WIDTH + Constants.Brick.HORIZONTAL_SPACING) - Constants.Brick.HORIZONTAL_SPACING;
        double left = 0.5 * (Constants.World.WIDTH - totalWidth);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final double x = j * (Constants.Brick.WIDTH + Constants.Brick.HORIZONTAL_SPACING) + left;
                final double y = i * (Constants.Brick.HEIGHT + Constants.Brick.VERTICAL_SPACING) + Constants.World.TOP_PADDING;
                //final Color color = Constants.Brick.INTERPOLATION_START_COLOR.interpolate(Constants.Brick.INTERPOLATION_END_COLOR, (double) i / rows);
                final Color color = Constants.Brick.COLORS_PER_ROW.getOrDefault(i, Color.WHITE);
                bricks.add(new Brick(x, y, Constants.Brick.WIDTH, Constants.Brick.HEIGHT, color));
            }
        }

        return bricks;
    }

    private static Set<Obstacle> constructObstacles() {
        Set<Obstacle> obstacles = new HashSet<>();

        obstacles.add(new Obstacle(List.of(new Point2D(100, 100), new Point2D(350, 350), new Point2D(550, 100)), Color.WHITE));

        obstacles.add(new Obstacle(List.of(new Point2D(100, 300), new Point2D(200, 400), new Point2D(250, 350),
                new Point2D(220, 200)), Color.WHITE));

        Obstacle obstacle = new Obstacle(List.of(new Point2D(600, 300), new Point2D(580, 320), new Point2D(850, 550),
                new Point2D(900, 500)), Color.WHITE);

        obstacle.translate(new Point2D(300, 0));
        obstacles.add(obstacle);

        obstacles.add(new Obstacle(List.of(new Point2D(0, 400), new Point2D(0, 800), new Point2D(600, 800)), Color.WHITE));

        return obstacles;
    }
}
