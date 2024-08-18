package com.github.berkbavas.breakout;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {
    }

    public final static class World {
        public static final double WIDTH = 800;
        public static final double HEIGHT = 800;
        public static final double TOP_PADDING = 150;
        public static final double COLLISION_IMPACT_FACTOR = 1.0;
    }

    public final static class Ball {
        public static final double RADIUS = 12;
        public static final double MIN_SPEED = 500;
        public static final double MAX_SPEED = 900;
        public static final double INITIAL_X = 0.5f * World.WIDTH;
        public static final double INITIAL_Y = 0.5f * World.HEIGHT;
        public static final Color COLOR = Color.WHITE;
    }

    public final static class Paddle {
        public static final double WIDTH = 300;
        public static final double HEIGHT = 30;
        public static final double INITIAL_X = 0.5f * (World.WIDTH - Paddle.WIDTH);
        public static final double INITIAL_Y = World.HEIGHT - 200;
        public static final Color COLOR = Color.WHITE;
        public static final double DO_NOT_MOVE_PADDLE_IF_BALL_TOO_CLOSE_OFFSET = 5.00;
        public static final double COLLISION_IMPACT_FACTOR = 2.5;
    }

    public final static class Brick {
        public static final double WIDTH = 64;
        public static final double HEIGHT = 24;
        public static final double HORIZONTAL_SPACING = 6;
        public static final double VERTICAL_SPACING = 8;
        public static final Color COLOR = Color.WHITE;
        public static final double COLLISION_IMPACT_FACTOR = 1.0;
    }

}
