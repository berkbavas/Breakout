package com.github.berkbavas.breakout;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {
    }

    public final static class GameBoard {
        public static final double WIDTH = 800;
        public static final double HEIGHT = 800;

    }

    public final static class Ball {
        public static final double RADIUS = 12;
        public static final double MIN_SPEED = 3000;
        public static final double MAX_SPEED = 4800;
        public static final double INITIAL_X = 0.5f * GameBoard.WIDTH;
        public static final double INITIAL_Y = 0.5f * GameBoard.HEIGHT;
        public static final Color COLOR = Color.WHITE;
    }

    public final static class Paddle {
        public static final double WIDTH = 320;
        public static final double HEIGHT = 25;
        public static final double INITIAL_X = 0.5f * (GameBoard.WIDTH - Paddle.WIDTH);
        public static final double INITIAL_Y = GameBoard.HEIGHT - 200;
        public static final Color COLOR = Color.WHITE;
    }

}
