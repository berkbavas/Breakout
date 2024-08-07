package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.Room;
import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.util.Stopwatch;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhysicsEngine {

    private final Stopwatch chronometer = new Stopwatch();
    private final Stopwatch performanceMonitor = new Stopwatch();
    private final GameObjects gameObjects;
    private final AtomicBoolean paused = new AtomicBoolean(false);

    private final Timer timer = new Timer("PhysicsEngine", true);
    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            tick();
        }
    };

    private Ball previousBall;

    public PhysicsEngine(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
        this.previousBall = gameObjects.getBall();
    }

    public void tick() {

        if (paused.get()) {
            return;
        }
        
        float remainingTime = chronometer.restart();

        // Iterations
        while (!Util.isFuzzyZero(remainingTime)) {
            Room room = gameObjects.getRoom();
            Ball ball = gameObjects.getBall();
            Paddle paddle = gameObjects.getPaddle();

            ArrayList<CollisionResult> candidates = new ArrayList<>();

            candidates.addAll(CollisionChecker.checkCollision(room, ball, previousBall, remainingTime));
            candidates.addAll(CollisionChecker.checkCollision(paddle, ball, previousBall, remainingTime));
            previousBall = gameObjects.getBall();
            remainingTime = process(candidates, remainingTime);
        }
    }

    private float process(ArrayList<CollisionResult> candidates, float ifps) {

        CollisionResult collision = findCandidate(candidates);
        float remainingTime;

        if (collision != null) { // There is a collision
            Ball newBall = collision.getNewBall();
            gameObjects.setBall(newBall);
            remainingTime = ifps - collision.getTimeToCollision();
        } else { // No collision
            Ball newBall = gameObjects.getBall().move(ifps);
            gameObjects.setBall(newBall);
            remainingTime = 0.0f;
        }
        remainingTime = Math.max(remainingTime, 0.0f);

        return remainingTime;
    }

    private CollisionResult findCandidate(ArrayList<CollisionResult> candidates) {
        float minConsumedTime = Float.MAX_VALUE;
        CollisionResult collision = null;

        for (CollisionResult candidate : candidates) {
            final float consumedTime = candidate.getTimeToCollision();
            if (consumedTime < minConsumedTime) {
                minConsumedTime = consumedTime;
                collision = candidate;
            }
        }

        return collision;
    }

    // x, y in [-1, 1] where (x,y) = (0,0) is the center and (x, y) = (-1, -1) is the left bottom
    public void onMouseMoved(float x, float y) {

    }

    public void start() {
        chronometer.reset();
        resume();
        timer.scheduleAtFixedRate(task, 0, 5);
    }


    public void stop() {
        timer.cancel();
    }


    public void pause() {
        paused.set(true);
    }


    public void resume() {
        paused.set(false);
    }
}
