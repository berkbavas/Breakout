package com.github.berkbavas.breakout.core;

import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.gui.GraphicsEngine;
import com.github.berkbavas.breakout.gui.ImGuiWindow;
import com.github.berkbavas.breakout.util.GameObjectConstructor;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;

public class Controller implements EventHandler<Event> {
    private final Scene scene;
    private final PhysicsManager engine;
    private final GraphicsEngine graphics;
    private final ImGuiWindow gui;
    private final EventDispatcher dispatcher;
    private final EventProcessor eventProcessor;
    private final boolean isDebugMode;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public Controller(boolean isDebugMode) {
        Locale.setDefault(Locale.US);

        GameObjects objects = GameObjectConstructor.construct(isDebugMode);

        graphics = new GraphicsEngine(objects, isDebugMode);
        TransformationHelper.initialize(objects.getWorld(), graphics.getCanvas());

        dispatcher = new EventDispatcher();
        eventProcessor = new EventProcessor(objects, dispatcher, isDebugMode);

        engine = new PhysicsManager(objects, isDebugMode);
        gui = new ImGuiWindow(objects, engine);

        var root = graphics.getRoot();
        var canvas = graphics.getCanvas();
        scene = new Scene(root, Color.BLACK);
        canvas.addEventHandler(Event.ANY, this);

        setupResizing();

        this.isDebugMode = isDebugMode;
    }

    public void start(Stage stage) {
        stage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
            final boolean hidden = newVal;
            if (hidden) {
                pause();
            } else {
                resume();
            }
        });

        if (isDebugMode) {
            gui.show(); // This line blocks until ImGui window is initialized.
        }

        stage.setScene(scene);
        stage.setTitle(isDebugMode ? "Debugger" : "Breakout");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.centerOnScreen();
        stage.show();

        timer.start();
    }

    @Override
    public void handle(Event event) {
        dispatcher.receiveEvent(event);
    }

    public void update() {
        var result = engine.update();
        eventProcessor.update();
        graphics.update(result);
        gui.update(result);
    }

    public void pause() {
        engine.pause();
        graphics.pause();
    }

    public void resume() {
        engine.resume();
        graphics.resume();
    }

    public void stop() {
        gui.close();
    }

    private void setupResizing() {
        StackPane root = graphics.getRoot();
        Canvas canvas = graphics.getCanvas();

        Bounds bounds = canvas.getLayoutBounds();

        ChangeListener<Number> resize = (observable, oldValue, newValue) -> {
            double scale = Math.min(root.getWidth() / bounds.getWidth(), root.getHeight() / bounds.getHeight());
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);

        };

        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);
    }

}
