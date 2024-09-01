package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.physics.handler.BreakoutDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DebuggerDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DragEventHandler;
import com.github.berkbavas.breakout.physics.handler.ThrowEventHandler;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.Simulator;
import com.github.berkbavas.breakout.util.Stopwatch;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhysicsManager {
    private final static double TICK_IN_SEC = 0.005;  //  Each tick is 0.005 seconds.

    private final GameObjects objects;
    private final Stopwatch chronometer = new Stopwatch();
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final Simulator simulator;
    private final DragEventHandler dragEventHandler;
    private final ThrowEventHandler throwEventHandler;
    private final boolean isDebugMode;
    private final VisualDebugger debugger;

    public PhysicsManager(GameObjects objects, EventDispatcher dispatcher, boolean isDebugMode) {
        final World world = objects.getWorld();
        final Set<Collider> colliders = objects.getColliders();
        final Ball ball = objects.getBall();

        this.objects = objects;
        this.simulator = new Simulator(world, colliders, ball);
        this.debugger = new VisualDebugger(objects);
        this.isDebugMode = isDebugMode;

        this.throwEventHandler = new ThrowEventHandler(ball, debugger);
        this.throwEventHandler.setEnabled(isDebugMode);

        if (isDebugMode) {
            dragEventHandler = new DebuggerDragEventHandler(objects);
        } else {
            dragEventHandler = new BreakoutDragEventHandler(objects);
        }

        dispatcher.addEventListener(throwEventHandler);
        dispatcher.addEventListener(dragEventHandler);
    }

    public void update() {
        if (paused.get()) {
            return;
        }

        throwEventHandler.update();
        dragEventHandler.update();

        final double deltaTime = TICK_IN_SEC;

        // Find all potential collisions along the direction of velocity of the ball and process.
        var result = simulator.update(deltaTime);

        // Check if a brick is hit in this particular tick.
        //updateBricks(result);

        // If debug mode is on, paint the output of algorithm for visual debugging.
        if (isDebugMode) {
            //var collisions = simulator.findEarliestCollisions(deltaTime);
            //debugger.paint(collisions);
            debugger.paint(result);
            debugger.paint(objects.getBall());
        }

        System.out.println(objects.getBall());
    }

//    private void updateBricks(Tick result) {
//        if (result.getStatus() == Tick.Status.COLLIDED) {
//            Set<Collision> collisions = result.getCollisions();
//            for (Collision collision : collisions) {
//                Collider collider = collision.getCollider();
//
//                if (collider instanceof Brick) {
//                    Brick brick = (Brick) collider;
//                    brick.setHit(true);
//                }
//            }
//        }
//    }

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
        paused.set(false);
    }

}
