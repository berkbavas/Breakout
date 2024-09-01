package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.util.Parameter;
import javafx.scene.paint.Color;

import java.util.HashMap;

public final class Constants {

    private Constants() {
    }

    public final static class World {
        public static final double WIDTH = 1200;
        public static final double HEIGHT = 800;
        public static final double TOP_PADDING = 100;
        public static final Color BACKGROUND_COLOR = Color.rgb(0, 0, 0);
        public static final Parameter RESTITUTION_FACTOR = new Parameter(0.5);
        public static final Parameter IMPULSION_FACTOR = new Parameter(0, 0.5, 0);
        public static final Parameter FRICTION_COEFFICIENT = new Parameter(0, 0, 0);
    }

    public final static class Ball {
        public static final double RADIUS = 12;
        public static final double MIN_SPEED = 500;
        public static final double MAX_SPEED = 600;
        public static final double INITIAL_X = 0.5f * World.WIDTH;
        public static final double INITIAL_Y = 0.5f * World.HEIGHT;
        public static final Color COLOR = Color.WHITE;
        public static final Parameter BALL_SHOULD_BE_STEADY_THRESHOLD = new Parameter(0, 5.0, 20.0);
        public static final Parameter MASS = new Parameter(0, 1, 10);
        public static final Parameter DO_NOT_REFLECT_VELOCITY_THRESHOLD = new Parameter(0, 50, 100);
    }

    public final static class Paddle {
        public static final double WIDTH = 300;
        public static final double HEIGHT = 28;
        public static final double INITIAL_X = 0.5f * (World.WIDTH - Paddle.WIDTH);
        public static final double INITIAL_Y = World.HEIGHT - 200;
        public static final Color COLOR = Color.WHITE;
        public static final double ARC_RADIUS = 4;
        public static final double COLLISION_IMPULSE_FACTOR = 0.0;
        public static final Parameter RESTITUTION_FACTOR = new Parameter(0.5);
        public static final Parameter IMPULSION_FACTOR = new Parameter(0, 0.5, 0);
        public static final Parameter FRICTION_COEFFICIENT = new Parameter(0, 0, 0);
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
        public static final Parameter RESTITUTION_FACTOR = new Parameter(0.5);
        public static final Parameter IMPULSION_FACTOR = new Parameter(0, 0.5, 0);
        public static final Parameter FRICTION_COEFFICIENT = new Parameter(0, 0, 0);

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
        public static final Parameter RESTITUTION_FACTOR = new Parameter(0.5);
        public static final Parameter IMPULSION_FACTOR = new Parameter(0, 0.5, 0);
        public static final Parameter FRICTION_COEFFICIENT = new Parameter(0, 0, 0);
    }
}
