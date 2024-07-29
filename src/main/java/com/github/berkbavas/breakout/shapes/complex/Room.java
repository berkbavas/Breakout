package com.github.berkbavas.breakout.shapes.complex;

import com.github.berkbavas.breakout.engine.StaticObject;
import com.github.berkbavas.breakout.engine.CollisionResult;
import com.github.berkbavas.breakout.shapes.base.Disk;
import com.github.berkbavas.breakout.shapes.base.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;

@Getter
public class Room {
    private final LeftWall leftWall;
    private final RightWall rightWall;
    private final TopWall topWall;
    private final BottomWall bottomWall;

    ArrayList<StaticObject> walls = new ArrayList<>();

    public Room(float x, float y, float width, float height) {
        leftWall = new LeftWall(x);
        rightWall = new RightWall(x + width);
        topWall = new TopWall(y);
        bottomWall = new BottomWall(y + height);

        walls.add(leftWall);
        walls.add(rightWall);
        walls.add(topWall);
        walls.add(bottomWall);
    }

    public ArrayList<CollisionResult> checkCollision(Disk ball) {
        ArrayList<CollisionResult> results = new ArrayList<>();

        for (StaticObject wall : walls) {
            wall.checkCollision(ball).ifPresent(results::add);
        }

        return results;
    }

    @AllArgsConstructor
    @Getter
    public static class LeftWall implements StaticObject {
        private float x;

        @Override
        public Optional<CollisionResult> checkCollision(Disk ball) {
            final boolean collides = ball.getLeft() <= x;

            CollisionResult result = null;

            if (collides) {
                result = new CollisionResult(this, new Vector2D(1.0f, 0.0f));
            }

            return Optional.ofNullable(result);
        }

        @Override
        public void processCollision(Disk ball, CollisionResult result, float ifps) {
            final float ballNextLeft = ball.calculateNextLeft(ifps);
            final float ballLeft = Math.max(ballNextLeft, x);

            ball.setLeft(ballLeft);
            ball.reflectVelocity(result.getCollisionNormal());
        }
    }

    @AllArgsConstructor
    @Getter
    public static class RightWall implements StaticObject {
        private float x;

        @Override
        public Optional<CollisionResult> checkCollision(Disk ball) {
            final boolean collides = x <= ball.getRight();

            CollisionResult result = null;

            if (collides) {
                result = new CollisionResult(this, new Vector2D(-1.0f, 0.0f));
            }

            return Optional.ofNullable(result);
        }

        @Override
        public void processCollision(Disk ball, CollisionResult result, float ifps) {
            final float ballNextRight = ball.calculateNextRight(ifps);
            final float ballRight = Math.min(ballNextRight, x);

            ball.setRight(ballRight);
            ball.reflectVelocity(result.getCollisionNormal());
        }

    }

    @AllArgsConstructor
    @Getter
    public static class TopWall implements StaticObject {
        private float y;

        @Override
        public Optional<CollisionResult> checkCollision(Disk ball) {
            final boolean collides = ball.getTop() <= y;

            CollisionResult result = null;

            if (collides) {
                result = new CollisionResult(this, new Vector2D(0.0f, 1.0f));
            }

            return Optional.ofNullable(result);
        }

        @Override
        public void processCollision(Disk ball, CollisionResult result, float ifps) {
            final float ballNextTop = ball.calculateNextTop(ifps);
            final float ballTop = Math.max(ballNextTop, y);

            ball.setTop(ballTop);
            ball.reflectVelocity(result.getCollisionNormal());
        }

    }

    @AllArgsConstructor
    @Getter
    public static class BottomWall implements StaticObject {
        private float y;

        @Override
        public Optional<CollisionResult> checkCollision(Disk ball) {
            final boolean collides = y <= ball.getBottom();

            CollisionResult result = null;

            if (collides) {
                result = new CollisionResult(this, new Vector2D(0.0f, -1.0f));
            }

            return Optional.ofNullable(result);
        }

        @Override
        public void processCollision(Disk ball, CollisionResult result, float ifps) {
            final float ballNextBottom = ball.calculateNextBottom(ifps);
            final float ballBottom = Math.min(ballNextBottom, y);

            ball.setBottom(ballBottom);
            ball.reflectVelocity(result.getCollisionNormal());
        }

    }

}
