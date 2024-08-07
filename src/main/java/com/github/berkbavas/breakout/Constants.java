package com.github.berkbavas.breakout;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {
    }

    public final static class GameBoard {
        public static final float WIDTH = 800;
        public static final float HEIGHT = 800;

    }

    public final static class Ball {
        public static final float RADIUS = 10;
        public static final float MIN_SPEED = 1500;
        public static final float MAX_SPEED = 1800;
        public static final float INITIAL_X = 0.5f * GameBoard.WIDTH;
        public static final float INITIAL_Y = 0.5f * GameBoard.HEIGHT;
        public static final Color COLOR = Color.WHITE;
    }

    public final static class Paddle {
        public static final float WIDTH = 320;
        public static final float HEIGHT = 25;
        public static final float INITIAL_X = 0.5f * (GameBoard.WIDTH - Paddle.WIDTH);
        public static final float INITIAL_Y = GameBoard.HEIGHT - 200;
        public static final Color COLOR = Color.WHITE;
    }

}
