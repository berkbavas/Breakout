package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Util;
import lombok.ToString;

import java.util.Optional;

public class PaddleActionListener {
    private final GameObjects gameObjects;
    private final Mouse mouse = new Mouse();

    public PaddleActionListener(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void onMouseDragged(double x, double y) {
        // x and y coordinates are normalized with respect to the world.
        // x, y in [0,1].
        mouse.dx += (x - mouse.x);
        mouse.dy += (y - mouse.y);
        mouse.x = x;
        mouse.y = y;
    }

    public void onMousePressed(double x, double y) {
        mouse.dx = 0;
        mouse.dy = 0;
        mouse.x = x;
        mouse.y = y;
    }

    public Optional<Point2D> getNewTopLeftPositionOfPaddleIfChanged() {

        if (Util.isFuzzyZero(mouse.dx)) {
            // If mouse has not moved since the last call, then no need to update the paddle.
            return Optional.empty();
        }

        Paddle paddle = gameObjects.getPaddle();

        // Maximum / Minimum of x coordinate that the position of the top left corner of the paddle can take.
        double xMin = 0.0;
        double xMax = gameObjects.getWorld().getWidth() - paddle.getWidth();
        double xPreviousTopLeft = paddle.getLeftTop().getX();
        double xDelta = mouse.dx * gameObjects.getWorld().getWidth();

        double xNewTopLeft = Util.clamp(xMin, xPreviousTopLeft + xDelta, xMax);
        double yNewTopLeft = gameObjects.getPaddle().getLeftTop().getY();

        // Consume dx
        mouse.dx = 0;

        return Optional.of(new Point2D(xNewTopLeft, yNewTopLeft));
    }

    @ToString
    private static class Mouse {
        private double x = 0;
        private double y = 0;
        private double dx = 0;
        private double dy = 0;
    }

}
