package com.github.berkbavas.breakout.physics.simulator.processor.resolver;

import com.github.berkbavas.breakout.math.Util;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.collision.InevitableCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.PresentCollision;
import com.github.berkbavas.breakout.physics.simulator.collision.ProspectiveCollision;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import com.github.berkbavas.breakout.physics.simulator.processor.FreeTick;
import com.github.berkbavas.breakout.physics.simulator.processor.SteadyTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;

import java.util.ArrayList;
import java.util.List;

public class PresentCollisionResolver extends CollisionResolver<PresentCollision> {
    private final List<InevitableCollision> inevitables = new ArrayList<>();
    private List<Collision> collisions;

    public PresentCollisionResolver(Ball ball, boolean isDebugMode) {
        super(ball, isDebugMode);
    }

    @Override
    public void load(List<Collision> collisions) {
        this.collisions = collisions;
        targets.clear();
        inevitables.clear();

        for (Collision collision : collisions) {
            if (collision instanceof PresentCollision) {
                PresentCollision target = (PresentCollision) collision;
                targets.add(target);
            }

            if (collision instanceof InevitableCollision) {
                InevitableCollision target = (InevitableCollision) collision;
                inevitables.add(target);
            }
        }
    }

    @Override
    public boolean isApplicable() {
        return !targets.isEmpty();
    }

    @Override
    public Tick<? extends Collision> resolve(double deltaTime) {
        if (ball.isSteady()) {
            return new SteadyTick<>(targets, deltaTime);
        }

        Vector2D velocity = ball.getVelocity();

        if (velocity.l2norm() == 0) {
            // Ball is still.
            // Call move() in order to update the velocity.
            ball.move(deltaTime);
            return new SteadyTick<>(targets, deltaTime);
        }

        boolean shouldCollide = shouldCollide(velocity);

        if (shouldCollide) {
            List<PresentCollision> subjects = filterCollisions(velocity);
            PresentCollision collision = subjects.get(0); // Most effective collision
            Vector2D normal = collision.getNormal();
            double restitutionFactor = collision.getCollider().getRestitutionFactor();

            if (isDebugMode) {
                ball.collide(normal, restitutionFactor);
            } else {
                ball.collide(normal);
            }

            return new CrashTick<>(subjects, normal, 0);
        }


        if (!inevitables.isEmpty()) {
            ProspectiveCollision earliest = findEarliestCollision(inevitables);
            double timeCanBeSpent = Util.clamp(0, earliest.getTimeToCollision(), deltaTime);
            Collider collider = earliest.getCollider();
            Vector2D normal = earliest.getNormal();
            ball.move(timeCanBeSpent);
            ball.collide(normal, collider.getRestitutionFactor());
            return new CrashTick<>(inevitables, normal, timeCanBeSpent);
        }

        ball.move(deltaTime);
        return new FreeTick<>(collisions, deltaTime);
    }

    public boolean shouldCollide(Vector2D velocity) {
        for (PresentCollision collision : targets) {
            Vector2D normal = collision.getNormal();
            if (normal.dot(velocity) < -Util.EPSILON) {
                return true;
            }
        }

        return false;
    }

    public List<PresentCollision> filterCollisions(Vector2D velocity) {
        if (velocity.l2norm() == 0) {
            throw new IllegalArgumentException("Velocity must be non-zero vector!");
        }

        List<PresentCollision> result = new ArrayList<>();

        for (PresentCollision collision : targets) {
            Vector2D normal = collision.getNormal();
            if (normal.dot(velocity) < -Util.EPSILON) {
                result.add(collision);
            }
        }

        if (result.isEmpty()) {
            throw new RuntimeException("Result cannot be empty!");
        }

        result.sort((c0, c1) -> {
            double cor0 = c0.getCollider().getRestitutionFactor();
            double cor1 = c1.getCollider().getRestitutionFactor();
            return Double.compare(cor0, cor1);
        });

        return result;
    }

}
