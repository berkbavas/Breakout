package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.physics.handler.BreakoutDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DebuggerDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DragEventHandler;
import com.github.berkbavas.breakout.physics.handler.ThrowEventHandler;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.Simulator;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.Set;

public class PhysicsManager {
    private final GameObjects objects;
    private final Simulator simulator;
    private final DragEventHandler dragEventHandler;
    private final ThrowEventHandler throwEventHandler;
    private final boolean isDebugMode;
    private final VisualDebugger debugger;
    private boolean paused = false;

    public PhysicsManager(GameObjects objects, EventDispatcher dispatcher, boolean isDebugMode) {
        final World world = objects.getWorld();
        final Set<Collider> colliders = objects.getColliders();
        final Ball ball = objects.getBall();

        this.objects = objects;
        this.simulator = new Simulator(world, colliders, ball, isDebugMode);
        this.debugger = new VisualDebugger(objects);
        this.isDebugMode = isDebugMode;

        this.throwEventHandler = new ThrowEventHandler(objects);
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
        if (paused) {
            return;
        }

        throwEventHandler.update();
        dragEventHandler.update();

        final double deltaTime = Constants.Physics.TICK_IN_SEC;

        var result = simulator.update(deltaTime);

        // Check if a brick is hit in this particular tick.
        updateBricks(result);

        // If debug mode is on, paint the output of algorithm for visual debugging.
        if (isDebugMode) {
            debugger.paint(result);
            debugger.paint(objects.getBall());
        }
    }

    private void updateBricks(Tick<? extends Collision> result) {
        if (result instanceof CrashTick) {
            var collisions = result.getCollisions();
            for (Collision collision : collisions) {
                Collider collider = collision.getCollider();

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
        paused = true;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

}
