package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.handler.BreakoutDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DebuggerDragEventHandler;
import com.github.berkbavas.breakout.physics.handler.DragEventHandler;
import com.github.berkbavas.breakout.physics.handler.ThrowEventHandler;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.Collider;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhysicsEngine {
    private final static double TICK_IN_SEC = 0.005;  //  Each tick is 0.005 seconds.

    private final Stopwatch chronometer = new Stopwatch();
    private final GameObjects objects;
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final TickProcessor tickProcessor;
    private final DragEventHandler dragEventHandler;
    private final ThrowEventHandler throwEventHandler;
    private final PaintCommandHandler painter;
    private final boolean isDebugMode;

    public PhysicsEngine(GameObjects objects, EventDispatcher dispatcher, boolean isDebugMode) {
        this.objects = objects;
        this.tickProcessor = new TickProcessor(objects);
        this.painter = OnDemandPaintCommandProcessor.getPaintCommandHandler(this);
        this.isDebugMode = isDebugMode;

        this.throwEventHandler = new ThrowEventHandler(objects, isDebugMode);

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

        double deltaTime = chronometer.getSeconds();
        chronometer.restart();

        deltaTime = TICK_IN_SEC; //  Each tick is deltaTime seconds.

        throwEventHandler.update();
        dragEventHandler.update();

        // World boundary vs ball position check
        tickProcessor.preTick();

        // Find all potential collisions along the direction of velocity of the ball and process.
        TickResult result = tickProcessor.nextTick(deltaTime);

        // Check if a brick is hit in this particular tick.
        updateBricks(result);

        // If debug mode is on, paint the output of algorithm for visual debugging.
        if (isDebugMode) {
            var collisions = tickProcessor.findEarliestCollisions();
            paint(collisions);
        }
    }

    private void updateBricks(TickResult result) {
        if (result.isCollided()) {
            Set<Collision> collisions = result.getCollisions();
            for (Collision collision : collisions) {
                Collider collider = collision.getCollider();

                if (collider instanceof Brick) {
                    Brick brick = (Brick) collider;
                    brick.setHit(true);
                }
            }
        }
    }

    private void paint(Set<Collision> collisions) {
        // Line(s) between potential collision contacts
        painter.clear();

        for (Collision collision : collisions) {
            Point2D p0 = collision.getContactPointOnBall();
            Point2D p1 = collision.getContactPointOnEdge();
            painter.drawLine(p0, p1, Color.RED);
        }

        // Velocity indicator
        Ball ball = objects.getBall();
        Point2D center = ball.getCenter();
        Vector2D dir = ball.getVelocity().normalized();
        double speed = ball.getSpeed() / 100;
        Point2D p0 = center.add(dir.multiply(ball.getRadius()));
        Point2D p1 = center.add(dir.multiply(speed * ball.getRadius()));
        painter.drawLine(p0, p1, Color.CYAN);
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
        paused.set(false);
    }

}
