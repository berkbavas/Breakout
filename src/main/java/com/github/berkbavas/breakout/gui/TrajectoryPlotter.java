package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.base.Collider;
import com.github.berkbavas.breakout.physics.node.base.Path;
import com.github.berkbavas.breakout.physics.simulator.Simulator;
import com.github.berkbavas.breakout.physics.simulator.processor.CrashTick;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class TrajectoryPlotter {
    private static final HashMap<Object, TrajectoryPlotterInner> INSTANCES = new HashMap<>();

    public static void plot(Object caller, Set<Collider> colliders, Point2D center, double radius, Vector2D velocity) {
        TrajectoryPlotterInner impl = getInstance(caller);
        impl.plot(colliders, center, radius, velocity);
    }

    public static void clear(Object caller) {
        TrajectoryPlotterInner impl = getInstance(caller);
        impl.clear();
    }

    public static void show(Object caller) {
        TrajectoryPlotterInner impl = getInstance(caller);
        impl.show();
    }

    public static void hide(Object caller) {
        TrajectoryPlotterInner impl = getInstance(caller);
        impl.hide();
    }

    private static TrajectoryPlotterInner getInstance(Object caller) {
        TrajectoryPlotterInner impl;

        if (INSTANCES.containsKey(caller)) {
            impl = INSTANCES.get(caller);
        } else {
            impl = new TrajectoryPlotterInner(GraphicsEngine.createHandler());
            INSTANCES.put(caller, impl);
        }

        return impl;
    }

    private static class TrajectoryPlotterInner {
        private final PaintCommandHandler painter;
        private List<PaintCommandHandler.PaintCommand> lastCommands = new ArrayList<>();

        public TrajectoryPlotterInner(PaintCommandHandler painter) {
            this.painter = painter;
        }

        public void plot(Set<Collider> colliders, Point2D center, double radius, Vector2D velocity) {
            Ball ball = new Ball(center, radius, velocity, Color.WHITE);

            Simulator simulator = new Simulator(colliders, ball, true);
            List<Point2D> vertices = new ArrayList<>(500);

            painter.clear();
            painter.stroke(ball.copy(), Color.LAWNGREEN, 2);

            int numberOfCollisions = 0;
            int numberOfIterations = 0;

            while (numberOfIterations < 500) {
                vertices.add(ball.getCenter());
                final double deltaTime = Constants.Physics.SIMULATION_RATIO[0];
                var result = simulator.process(deltaTime);

                if (result instanceof CrashTick) {
                    numberOfCollisions++;
                    painter.stroke(ball.copy(), Color.LAWNGREEN, 2);

                    if (numberOfCollisions >= 10) {
                        break;
                    }
                }

                numberOfIterations++;
            }

            painter.stroke(new Path(vertices, Color.RED), 2);

            lastCommands = painter.copyCommands();
        }

        public void show() {
            painter.setCommands(lastCommands);
        }

        public void hide() {
            painter.clear();
        }

        public void clear() {
            painter.clear();
        }
    }

}
