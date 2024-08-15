package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class TickProcessorResult {
    // The new state of the ball is updated after each tick.
    // This member is updated regardless of a collision.
    // If a collision happens, then TickProcessor calculates new velocity and position of the ball.
    // If there is no collision in the given time, then TickProcessor move the ball according to its velocity and
    // given time ifps.
    private final Ball newBall;

    // The earliest collisions among all possible collisions.
    // This member is 'Set' because the ball may collide with several edges at the same time,
    // which is a rare but probable occasion.
    // Note that the set of collisions will always contain the earliest collisions,
    // regardless of whether a collision happens in the given time, or tick.
    private final Set<Collision> collisions = new HashSet<>();

    // States whether a collision will happen in the given delta time.
    private final boolean collided;

    private final Vector2D collisionNormal;

    // This variable stands for the consumed time in this particular tick.
    private final double consumedTime;

    public TickProcessorResult(Ball newBall, double consumedTime, Vector2D collisionNormal) {
        this.newBall = newBall;
        this.consumedTime = consumedTime;
        this.collisionNormal = collisionNormal;
        this.collided = true;
    }

    public TickProcessorResult(Ball newBall, double consumedTime) {
        this.newBall = newBall;
        this.consumedTime = consumedTime;
        this.collisionNormal = null;
        this.collided = false;
    }


    public void addCollision(Collision collision) {
        this.collisions.add(collision);
    }

    public void addCollisions(Set<Collision> collisions) {
        this.collisions.addAll(collisions);
    }
}
