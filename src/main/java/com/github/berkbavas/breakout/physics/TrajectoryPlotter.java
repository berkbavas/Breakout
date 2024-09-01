package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Path;
import com.github.berkbavas.breakout.physics.simulator.Simulator;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class TrajectoryPlotter {
    private final PaintCommandHandler painter;
    private World world;
    private Set<Collider> colliders;
    private Ball ball;
    private int numberOfIterations = 1000;
    private double deltaTime = 0.005;
    private int maximumCollisionCount = 10;

    public TrajectoryPlotter(World world, Set<Collider> colliders, Ball ball) {
        this.world = world;
        this.colliders = colliders;
        this.ball = ball;
        this.painter = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
    }

    public void plotTrajectory() {
        Simulator simulator = new Simulator(world, colliders, ball);

        List<Point2D> vertices = new ArrayList<>(numberOfIterations);
        painter.clear();

        int numberOfCollisions = 0;

        for (int i = 0; i < numberOfIterations; ++i) {
            vertices.add(ball.getCenter());
            var result = simulator.update(deltaTime);

            if (result instanceof CrashTick) {
                if (++numberOfCollisions >= maximumCollisionCount) {
                    break;
                }
                painter.stroke(ball.copy(), Color.LAWNGREEN);
            }
        }

        painter.stroke(new Path(vertices, Color.RED));
    }

    public void clearTrajectory() {
        painter.clear();
    }
}
