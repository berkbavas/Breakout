package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.math.Vector2D;
import javafx.scene.paint.Color;

import java.util.HashMap;

public final class Constants {

    private Constants() {
    }

    public final static class World {
        public static final double WIDTH = 1280;
        public static final double HEIGHT = 720;
        public static final double TOP_PADDING = 100;
        public static final Color BACKGROUND_COLOR = Color.rgb(32, 32, 32);
        public static final double FRICTION_COEFFICIENT = 0.05;
    }

    public final static class Ball {
        public static final double RADIUS = 12;
        public static final double MIN_SPEED = 500;
        public static final double MAX_SPEED = 700;
        public static final double INITIAL_X = 0.5f * World.WIDTH;
        public static final double INITIAL_Y = 0.5f * World.HEIGHT;
        public static final Color COLOR = Color.WHITE;
        public static double RESTITUTION_FACTOR = 0.6;
        public static double DO_NOT_BOUNCE_SPEED_THRESHOLD = 8; // Should be a function of gravity
        public static double DO_NOT_REFLECT_ANGLE_THRESHOLD = 8;
    }

    public final static class Paddle {
        public static final double WIDTH = 300;
        public static final double HEIGHT = 28;
        public static final double INITIAL_X = 0.5f * (World.WIDTH - Paddle.WIDTH);
        public static final double INITIAL_Y = World.HEIGHT - 200;
        public static final Color COLOR = Color.WHITE;
        public static final double ARC_RADIUS = 4;
        public static double FRICTION_COEFFICIENT = 0;
    }

    public final static class Brick {
        public static final double WIDTH = 80;
        public static final double HEIGHT = 32;
        public static final double HORIZONTAL_SPACING = 3;
        public static final double VERTICAL_SPACING = 3;
        public static final double ARC_RADIUS = 4;
        public static final HashMap<Integer, Color> COLORS_PER_ROW = new HashMap<>();
        public static final Color INTERPOLATION_START_COLOR = Color.rgb(90, 40, 250);
        public static final Color INTERPOLATION_END_COLOR = Color.rgb(96, 245, 145);
        public static double FRICTION_COEFFICIENT = 0;

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
        public static double FRICTION_COEFFICIENT = 0.05;
    }

    public final static class Physics {
        public static final double TICK_IN_SEC = 0.001;  //  Each tick is 10 ms
        public static Vector2D GRAVITY = new Vector2D(0, 2000);
    }
}
