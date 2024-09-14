package com.github.berkbavas.breakout.core;

import javafx.scene.paint.Color;

import java.util.HashMap;

public final class Constants {

    private Constants() {
    }

    public final static class World {
        public static final double WIDTH = 1280;
        public static final double HEIGHT = 720;
        public static final double TOP_PADDING = 64;
        public static final Color BACKGROUND_COLOR = Color.rgb(24, 24, 24);
        public static final float[] FRICTION_COEFFICIENT = {0.05f};
    }

    public final static class Ball {
        public static final double RADIUS = 12;
        public static final double MIN_SPEED = 500;
        public static final double MAX_SPEED = 700;
        public static final double INITIAL_X = 0.5f * World.WIDTH;
        public static final double INITIAL_Y = 0.5f * World.HEIGHT;
        public static final Color COLOR = Color.WHITE;
        public static float[] RESTITUTION_FACTOR = {0.6f};
        public static float[] DO_NOT_BOUNCE_SPEED_THRESHOLD = {8.0f}; // Should be a function of gravity
    }

    public final static class Paddle {
        public static final double WIDTH = 192;
        public static final double HEIGHT = 28;
        public static final double INITIAL_X = 0.5f * (World.WIDTH - Paddle.WIDTH);
        public static final double INITIAL_Y = World.HEIGHT - 100;
        public static final Color COLOR = Color.WHITE;
        public static final double ARC_RADIUS = 0;
        public static float[] FRICTION_COEFFICIENT = {0.0f};
    }

    public final static class Brick {
        public static final double WIDTH = 82;
        public static final double HEIGHT = 32;
        public static final double HORIZONTAL_SPACING = 2;
        public static final double VERTICAL_SPACING = 2;
        public static final double ARC_RADIUS = 0;
        public static final HashMap<Integer, Color> COLORS_PER_ROW = new HashMap<>();
        public static final Color INTERPOLATION_START_COLOR = Color.rgb(90, 40, 250);
        public static final Color INTERPOLATION_END_COLOR = Color.rgb(96, 245, 145);
        public static float[] FRICTION_COEFFICIENT = {0.0f};

        static {
            COLORS_PER_ROW.put(0, Color.rgb(255, 0, 0));
            COLORS_PER_ROW.put(1, Color.rgb(255, 64, 0));
            COLORS_PER_ROW.put(2, Color.rgb(255, 127, 0));
            COLORS_PER_ROW.put(3, Color.rgb(255, 196, 0));
            COLORS_PER_ROW.put(4, Color.rgb(255, 255, 0));
            COLORS_PER_ROW.put(5, Color.rgb(220, 255, 0));
            COLORS_PER_ROW.put(6, Color.rgb(170, 255, 0));
            COLORS_PER_ROW.put(7, Color.rgb(127, 255, 0));
        }
    }

    public final static class Obstacle {
        public static float[] FRICTION_COEFFICIENT = {0.05f};
    }

    public final static class Physics {
        public static final float[] SIMULATION_RATIO = {0.0125f};
        public static final float[] GRAVITY = {500.0f};
        public static final float[] NET_FORCE_CALCULATOR_TOLERANCE = {0.001f};
    }
}
