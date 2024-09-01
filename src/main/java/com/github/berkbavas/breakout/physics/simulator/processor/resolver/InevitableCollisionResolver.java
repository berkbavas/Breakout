package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;

import java.util.List;

public class InevitableCollisionResolver extends CollisionResolver<InevitableCollision> {
    public InevitableCollisionResolver(Ball ball) {
        super(ball);
    }

    @Override
    public void load(List<Collision> collisions) {
        targets.clear();

        for (Collision collision : collisions) {
            if (collision instanceof InevitableCollision) {
                InevitableCollision target = (InevitableCollision) collision;
                targets.add(target);
            }
        }
    }

    @Override
    public boolean isApplicable() {
        return !targets.isEmpty();
    }

    @Override
    public CrashTick<InevitableCollision> resolve(double deltaTime) {
        ProspectiveCollision earliest = findEarliestCollision(targets);
        Collider collider = earliest.getCollider();
        double timeToCollision = earliest.getTimeToCollision();
        Vector2D velocity = ball.getVelocity();
        Vector2D normal = calculateCollisionNormal(velocity);

        if (normal.l2norm() == 0) {
            // TODO: Think about a smart solution for this case.
            ball.move(timeToCollision);
        }
        else
        {
            ball.move(timeToCollision);
            ball.collide(normal, collider.getRestitutionFactor());
        }

        return new CrashTick<>(targets, normal, timeToCollision);
    }


    public Vector2D calculateCollisionNormal(Vector2D velocity) {
        if (velocity.l2norm() == 0) {
            throw new IllegalArgumentException("Velocity must be non-zero vector!");
        }

        Vector2D result = new Vector2D(0, 0);

        for (InevitableCollision collision : targets) {
            Vector2D normal = collision.getNormal();

            // We only consider normals whose dot product with the velocity vector is negative.
            // Otherwise, reflection of the velocity vector w.r.t. collision normal does not make any sense.
            if (normal.dot(velocity) < -Util.EPSILON) {
                result = result.add(normal);
            }
        }

        return result.normalized();
    }
}
