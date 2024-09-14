package com.github.berkbavas.breakout.gui;

import com.github.berkbavas.breakout.core.Constants;
import com.github.berkbavas.breakout.core.GameObjects;
import com.github.berkbavas.breakout.core.PhysicsManager;
import com.github.berkbavas.breakout.math.Vector2D;
import com.github.berkbavas.breakout.physics.handler.ThrowEventHandler;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.simulator.collision.Collision;
import com.github.berkbavas.breakout.physics.simulator.processor.StationaryTick;
import com.github.berkbavas.breakout.physics.simulator.processor.Tick;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;

import java.util.ArrayList;

public class ImGuiWindow extends Application {
    private final Ball ball;
    private final PhysicsManager manager;
    private final Object lock = new Object();
    private volatile boolean close = false;
    private Tick<? extends Collision> tickResult = new StationaryTick<>(new ArrayList<>(), 0);
    private volatile boolean initialized = false;
    private boolean runOnce = true;

    public ImGuiWindow(GameObjects objects, PhysicsManager manager) {
        this.ball = objects.getBall();
        this.manager = manager;
    }

    @Override
    protected void initImGui(final Configuration config) {
        super.initImGui(config);
        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null); // We don't want to save .ini file
        io.setConfigViewportsNoTaskBarIcon(true);
    }

    @Override
    protected void configure(Configuration config) {
        config.setWidth(520);
        config.setHeight(800);
        config.setTitle("ImGui Window");
    }

    public void update(Tick<? extends Collision> tickResult) {
        this.tickResult = tickResult;
    }

    public void show() {
        new Thread(() -> Application.launch(this)).start();

        synchronized (lock) {
            while (!initialized) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void close() {
        close = true;
    }

    @Override
    public void process() {
        if (close) {
            System.exit(0);
        }

        if (runOnce) {
            synchronized (lock) {
                runOnce = false;
                initialized = true;
                lock.notifyAll();
            }
        }

        ImGui.setNextWindowSize(440, 720, ImGuiCond.FirstUseEver);

        if (ImGui.begin("Debugger")) {

            if (!ImGui.collapsingHeader("Stats")) {
                ImGui.text(String.format("Position   : %10.2f, %10.2f", ball.getCenter().getX(), ball.getCenter().getY()));
                ImGui.text(String.format("Velocity   : %10.4f, %10.4f", ball.getVelocity().getX(), ball.getVelocity().getY()));
                ImGui.text(String.format("Net Force  : %10.2f, %10.2f", ball.getNetForce().getX(), ball.getNetForce().getY()));
                ImGui.text(String.format("Speed      : %10.3f", ball.getSpeed()));
            }

            if (!ImGui.collapsingHeader("Tick Result")) {
                ImGui.text(tickResult.toString());
            }

            if (!ImGui.collapsingHeader("Ball")) {
                float[] velocity = {(float) ball.getVelocity().getX(), (float) ball.getVelocity().getY()};

                if (ImGui.sliderFloat2("Velocity", velocity, -1000, 1000)) {
                    ball.setVelocity(new Vector2D(velocity[0], velocity[1]));
                }

                ImGui.sliderFloat("Restitution", Constants.Ball.RESTITUTION_FACTOR, 0.0f, 1.0f);
                ImGui.sliderFloat("Bounce Speed Threshold", Constants.Ball.DO_NOT_BOUNCE_SPEED_THRESHOLD, 0.0f, 50.0f);
            }

            if (!ImGui.collapsingHeader("Physics")) {
                ImGui.sliderFloat("Obstacle Friction", Constants.Obstacle.FRICTION_COEFFICIENT, 0.0f, 1.0f);
                ImGui.sliderFloat("World Friction", Constants.World.FRICTION_COEFFICIENT, 0.0f, 1.0f);
                ImGui.sliderFloat("Net Force Calculator Tolerance", Constants.Physics.NET_FORCE_CALCULATOR_TOLERANCE, 0.0000f, 0.5f);

                if (ImGui.sliderFloat("Gravity", Constants.Physics.GRAVITY.getAsArray(), 0.000f, 3000.0f)) {
                    Constants.Physics.GRAVITY.update();
                }
            }

            if (!ImGui.collapsingHeader("Simulation")) {
                ImGui.sliderFloat("Simulation Speed", Constants.Physics.SIMULATION_RATIO, 0.0005f, 0.05f);

                if (ImGui.checkbox("Visual Debugger", VisualDebugger.ENABLED.get())) {
                    VisualDebugger.ENABLED.set(!VisualDebugger.ENABLED.get());
                }

                if (ImGui.checkbox("Trajectory Plotting", ThrowEventHandler.PLOT_TRAJECTORY_ENABLED.get())) {
                    ThrowEventHandler.PLOT_TRAJECTORY_ENABLED.set(!ThrowEventHandler.PLOT_TRAJECTORY_ENABLED.get());
                }

                if (manager.isPaused()) {
                    if (ImGui.button("> Resume")) {
                        manager.resume();
                    }
                    ImGui.sameLine();
                    if (ImGui.button("Next Step")) {
                        manager.next();
                    }

                } else {
                    if (ImGui.button("[] Pause")) {
                        manager.pause();
                    }
                }
            }
        }

        ImGui.end();
    }
}