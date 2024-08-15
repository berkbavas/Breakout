package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.SharedState;
import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.StaticNode;
import com.github.berkbavas.breakout.util.Stopwatch;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine {

    private final Stopwatch chronometer = new Stopwatch();
    private final Stopwatch performanceMonitor = new Stopwatch();
    private final GameObjects gameObjects;
    private final AtomicBoolean paused = new AtomicBoolean(false);

    private final Timer timer = new Timer("PhysicsEngine", true);

    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            update();
        }
    };

    private final CollisionDetector collisionDetector;
    private final TickProcessor tickProcessor;

    private final SharedState sharedState;

    public Engine(GameObjects gameObjects, SharedState sharedState) {
        this.gameObjects = gameObjects;
        this.sharedState = sharedState;

        this.collisionDetector = new CollisionDetector(gameObjects);
        this.tickProcessor = new TickProcessor(gameObjects);
    }

    public void update() {
        if (paused.get()) {
            return;
        }

        double deltaTime = chronometer.getSeconds();
        chronometer.restart();

        do {
            // Find all potential collisions along the direction of velocity of the ball.
            Set<Collision> potentialCollisions = collisionDetector.findPotentialCollisions();

            // Process the potential collisions.
            TickProcessorResult result = tickProcessor.process(potentialCollisions, deltaTime);

            // The state of the ball is updated each tick regardless of whether a collision is happened.
            Ball newBall = result.getNewBall();
            sharedState.setTickProcessorResult(result);
            gameObjects.setBall(newBall);

            deltaTime -= result.getConsumedTime();

            // Check if a brick is hit in this particular tick.
            if (result.isCollided()) {
                Set<Collision> collisions = result.getCollisions();
                for (Collision collision : collisions) {
                    StaticNode collider = collision.getCollider();

                    if (collider instanceof Brick) {
                        Brick brick = (Brick) collider;
                        brick.setHit(true);
                    }
                }
            }

        } while (deltaTime > 0);
    }


    // x, y in [-1, 1] where (x,y) = (0,0) is the center and (x, y) = (-1, -1) is the left bottom
    public void onMouseMoved(double x, double y) {

    }

    public void start() {
        chronometer.start();
        performanceMonitor.start();
        resume();

        //timer.scheduleAtFixedRate(task, 0 ,5);
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
