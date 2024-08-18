package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.node.*;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class GameObjects {
    private final World world;
    private final ArrayList<Brick> bricks;

    @Setter
    private volatile Ball ball;
    @Setter
    private volatile Paddle paddle;

    public void moveBall(double deltaTime) {
        Ball newBall = ball.move(deltaTime);
        setBall(newBall);
    }

    public void collideBall(StaticNode collider , Vector2D collisionNormal, double timeToCollision) {
        Ball newBall = ball.collide(collisionNormal, timeToCollision, collider.getCollisionImpactFactor());
        setBall(newBall);
    }

    public void updatePaddle(Point2D newTopLeft) {
        Paddle newPaddle = paddle.getNewPaddleByTakingCareOfCollision(newTopLeft, ball);
        setPaddle(newPaddle);
    }

}
