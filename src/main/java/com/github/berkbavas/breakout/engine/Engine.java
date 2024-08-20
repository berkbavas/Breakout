package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.SharedState;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.ColliderNode;
import com.github.berkbavas.breakout.util.Stopwatch;
import lombok.Getter;

import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine {
    private static final long TICK_STEP_IN_MS = 5;

    private final Stopwatch chronometer = new Stopwatch();
    private final Stopwatch performanceMonitor = new Stopwatch();
    private final GameObjects gameObjects;
    private final AtomicBoolean paused = new AtomicBoolean(false);
    @Getter
    private final PaddleActionListener paddleActionListener;
    private final TickProcessor tickProcessor;

    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            update();
        }
    };

    private final SharedState sharedState;

    public Engine(GameObjects gameObjects, SharedState sharedState) {
        this.gameObjects = gameObjects;
        this.sharedState = sharedState;
        this.paddleActionListener = new PaddleActionListener(gameObjects);
        this.tickProcessor = new TickProcessor(gameObjects);
    }

    public void update() {
        if (paused.get()) {
            return;
        }

        double deltaTime = chronometer.getSeconds();
        chronometer.restart();

        deltaTime = TICK_STEP_IN_MS / 1000.0; // In seconds

        // World vs Ball boundary check
        tickProcessor.preprocess();

        // Update paddle
       // paddleActionListener.getNewTopLeftPositionOfPaddleIfChanged().ifPresent(tickProcessor::updatePaddle);

        // Find all potential collisions along the direction of velocity of the ball and process.
        TickResult result = tickProcessor.nextTick(deltaTime);

        sharedState.setTickResult(result);

        // Check if a brick is hit in this particular tick.
        updateBricks(result);
    }

    public void updateBricks(TickResult result) {
        if (result.isCollided()) {
            Set<Collision> collisions = result.getCollisions();
            for (Collision collision : collisions) {
                ColliderNode collider = collision.getCollider();

                if (collider instanceof Brick) {
                    Brick brick = (Brick) collider;
                    brick.setHit(true);
                }
            }
        }
    }

    public void start() {
        resume();
    }

    public void stop() {
        paused.set(true);
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        chronometer.start();
        performanceMonitor.start();
        paused.set(false);
    }
}
