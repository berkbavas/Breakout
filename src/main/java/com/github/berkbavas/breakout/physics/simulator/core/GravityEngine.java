package com.github.berkbavas.breakout.physics.simulator.core;

import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Vertex;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class GravityEngine {
    private final Set<Collider> colliders;
    private final Ball ball;

    @Setter
    private Vector2D gravity = new Vector2D(0, 4000);

    public GravityEngine(Set<Collider> colliders, Ball ball) {
        this.colliders = colliders;
        this.ball = ball;
    }

    public void update(TickResult result, double deltaTime) {
        if (result.getStatus() == TickResult.Status.MOVING) {
            // Ball is free. Can we apply the gravity to the ball immediately?
            ball.setAcceleration(gravity);

        } else if (result.getStatus() == TickResult.Status.COLLIDED) {
            Vector2D normal = result.getCollisionNormal();
            Vector2D acceleration = gravity.rejectionOf(normal);  // We need rejection, not projection.
            ball.setAcceleration(acceleration);

        } else if (result.getStatus() == TickResult.Status.STEADY) {
            // TODO: Implement this case!
        }
    }

    public Vector2D calculateInteractionNormal(Set<Pair<Collider, Vertex>> interactions) {
        Vector2D result = new Vector2D(0, 0);

        for (Pair<Collider, Vertex> pair : interactions) {
            Collider collider = pair.getKey();
            Vertex vertex = pair.getValue();
            Vector2D normal = collider.getNormalOf(vertex.getOwner());
            // We will accept this normal if it physically makes sense.
            // We expect a non-negligible rejection of the gravity vector, so we look at the scalar product.
            if (Vector2D.dot(normal, gravity) < 0) {
                // We accept this normal.
                result = result.add(normal);
            }
        }

        return result.normalized();
    }
}
