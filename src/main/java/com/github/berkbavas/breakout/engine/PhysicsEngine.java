package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.Stopwatch;
import com.github.berkbavas.breakout.shapes.base.Disk;
import com.github.berkbavas.breakout.shapes.base.Point2D;
import com.github.berkbavas.breakout.shapes.base.Rectangle;
import com.github.berkbavas.breakout.shapes.base.Vector2D;
import com.github.berkbavas.breakout.shapes.complex.Ball;
import com.github.berkbavas.breakout.shapes.complex.Room;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PhysicsEngine {

    private final Timer timer = new Timer("PhysicsEngine", true);
    private final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            tick();
        }
    };

    private final Stopwatch chronometer = new Stopwatch();
    private Room room;
    private ArrayList<Rectangle> bricks;
    private Rectangle paddle;
    private Disk ball;
    private boolean paused = false;
    private Disk nextBall = new Disk(0, 0, 0);

    public PhysicsEngine(Room room, ArrayList<Rectangle> bricks, Rectangle paddle, Disk ball) {
        this.room = room;
        this.bricks = bricks;
        this.paddle = paddle;
        this.ball = ball;
    }

    public void tick() {
        final float ifps = chronometer.restart();

        nextBall.updateFrom(ball);
        nextBall.next(ifps);


        ArrayList<CollisionResult> results = room.checkCollision(nextBall);
        process(results, ball, ifps);

        System.out.printf("x: %.2f, y: %.2f, ifps: %.4f, ThreadId: %d %n", ball.getX(), ball.getY(), ifps,
                Thread.currentThread().getId());
    }

    public static void process(ArrayList<CollisionResult> results, Disk ball, float ifps) {

        for (CollisionResult result : results) {
            StaticObject obj = result.getCollidingObject();
            synchronized (ball.lock()) {
                obj.processCollision(ball, result, ifps);
            }
        }

        if (results.isEmpty()) {
            synchronized (ball.lock()) {
                ball.next(ifps);
            }
        }
    }

    public void start() {
        chronometer.reset();
        resume();
        timer.scheduleAtFixedRate(task, 0, 5);
    }

    public void cancel() {
        timer.cancel();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

}
