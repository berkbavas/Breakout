package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.PhysicsEngine;
import com.github.berkbavas.breakout.gui.ImageGenerator;
import com.github.berkbavas.breakout.shapes.base.Disk;
import com.github.berkbavas.breakout.shapes.base.Rectangle;
import com.github.berkbavas.breakout.shapes.base.Vector2D;
import com.github.berkbavas.breakout.shapes.complex.Room;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;

public class Controller {

    private final PhysicsEngine engine;
    private final ImageGenerator ig;

    private final Room room;
    private final Disk ball;

    private final AnimationTimer timer;

    public Controller() {

        room = new Room(0, 0, Constants.GAME_BOARD_WIDTH, Constants.GAME_BOARD_HEIGHT);
        ball = new Disk(0.5f * Constants.GAME_BOARD_WIDTH, 0.5f * Constants.GAME_BOARD_HEIGHT, Constants.BALL_RADIUS);
        ball.setVelocity(new Vector2D(-656, -288));

        engine = new PhysicsEngine(room, null, null, ball);
        ig = new ImageGenerator(ball);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                ig.update();
            }
        };

    }

    public Node getContainer() {
        return ig.getContainer();
    }

    public void start() {
        engine.start();
        timer.start();
    }

    public void cancel() {
        engine.cancel();
    }

    public void pause() {
        engine.pause();
    }

    public void resume() {
        engine.resume();
    }

}
