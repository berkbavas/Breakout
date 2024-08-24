package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.GameObjects;
import com.github.berkbavas.breakout.event.Event;
import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.event.EventListener;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.Brick;
import com.github.berkbavas.breakout.physics.node.Collider;
import com.github.berkbavas.breakout.util.Stopwatch;
import javafx.scene.paint.Color;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhysicsEngine implements EventListener {
    private final static double TICK_IN_SEC = 0.005;  //  Each tick is 0.005 seconds.

    private final Stopwatch chronometer = new Stopwatch();
    private final GameObjects objects;
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final EventDispatcher eventDispatcher;
    private final TickProcessor tickProcessor;
    private final DragEventHandler dragEventHandler;
    private final ThrowEventHandler throwEventHandler;
    private final PaintCommandHandler painter;
    private final boolean isDebugMode;

    public PhysicsEngine(GameObjects objects, EventDispatcher eventDispatcher, boolean isDebugMode) {
        this.objects = objects;
        this.eventDispatcher = eventDispatcher;
        this.tickProcessor = new TickProcessor(objects);
        this.dragEventHandler = new DragEventHandler(objects);
        this.throwEventHandler = new ThrowEventHandler(objects);
        this.painter = OnDemandPaintCommandProcessor.getPaintCommandHandler(this);
        this.isDebugMode = isDebugMode;

        eventDispatcher.addEventListener(throwEventHandler);
    }

    public void update() {
        if (paused.get()) {
            return;
        }

        double deltaTime = chronometer.getSeconds();
        chronometer.restart();

        deltaTime = TICK_IN_SEC; //  Each tick is deltaTime seconds.

        throwEventHandler.update();

        // World boundary vs ball position check
        tickProcessor.preTick();

        // Process events
        eventDispatcher.query(this).ifPresent(this::processEvent);

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

    private void processEvent(Event event) {
        if (isDebugMode) {
            if (event.getTarget() == objects.getBall()) {
                // Do nothing if the target is ball because
                // these events will be processed by listen() and update() methods.
            } else if (event.getType() == EventType.MOUSE_DRAGGED) {
                dragEventHandler.translate(event.getTarget(), event.getDelta());
            }

        } else {
            if (event.getType() == EventType.MOUSE_MOVED) {
                dragEventHandler.translate(event.getTarget(), event.getDelta());
            }
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

    @Override
    public void listen(Event event) {

    }
}
