package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.util.Stopwatch;

import java.util.Set;
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

    private final CollisionDetector collisionDetector = new CollisionDetector();
    private final TickProcessor tickProcessor = new TickProcessor();

    public PhysicsEngine(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void tick() {

        if (paused.get()) {
            return;
        }

        final double ifps = chronometer.getSeconds();
        chronometer.restart();

        // Find all possible collisions regardless of time.
        Set<Collision> allCollisions = collisionDetector.findCollisions(gameObjects);

        TickProcessorResult result = tickProcessor.process(gameObjects, allCollisions, ifps);

        // Next ball
        Ball nextBall = result.getNextBall();
        gameObjects.setBall(nextBall);
    }


    // x, y in [-1, 1] where (x,y) = (0,0) is the center and (x, y) = (-1, -1) is the left bottom
    public void onMouseMoved(double x, double y) {

    }

    public void start() {
        chronometer.start();
        performanceMonitor.start();
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
