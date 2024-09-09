package com.github.berkbavas.breakout.physics;

import com.github.berkbavas.breakout.Constants;
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
    private double deltaTime = Constants.Physics.TICK_IN_SEC;
    private int maximumNumberOfIterations = 500;
    private int maximumNumberOfCollisions = 8;

    public TrajectoryPlotter(World world, Set<Collider> colliders, Ball ball) {
        this.world = world;
        this.colliders = colliders;
        this.ball = ball;
        this.painter = OnDemandPaintCommandProcessor.getNextPaintCommandHandler();
    }

    public void plotTrajectory() {
        Simulator simulator = new Simulator(world, colliders, ball, true);

        List<Point2D> vertices = new ArrayList<>(maximumNumberOfIterations);

        painter.clear();
        painter.stroke(ball.copy(), Color.WHITE, 2);

        int numberOfCollisions = 0;
        int numberOfIterations = 0;

        while (numberOfIterations < maximumNumberOfIterations) {
            vertices.add(ball.getCenter());
            var result = simulator.update(deltaTime);

            if (result instanceof CrashTick) {
                numberOfCollisions++;
                painter.stroke(ball.copy(), Color.LAWNGREEN, 2);

                if (numberOfCollisions >= maximumNumberOfCollisions) {
                    break;
                }
            }

            numberOfIterations++;
        }

        painter.stroke(new Path(vertices, Color.RED), 2);
    }

    public void clearTrajectory() {
        painter.clear();
    }
}
