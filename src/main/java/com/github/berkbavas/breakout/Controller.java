package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.graphics.GraphicsEngine;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.physics.PhysicsManager;
import com.github.berkbavas.breakout.util.GameObjectConstructor;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class Controller implements EventHandler<Event> {
    private final Scene scene;
    private final PhysicsManager engine;
    private final GraphicsEngine gui;
    private final GameObjects objects;
    private final EventDispatcher dispatcher;
    private final boolean isDebugMode;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public Controller(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
        objects = GameObjectConstructor.construct(isDebugMode);

        gui = new GraphicsEngine(objects);

        OnDemandPaintCommandProcessor.initialize(gui);
        TransformationHelper.initialize(objects.getWorld(), gui.getCanvas());

        StackPane root = gui.getRoot();
        Canvas canvas = gui.getCanvas();
        canvas.addEventHandler(Event.ANY, this);

        scene = new Scene(root, Color.BLACK);
        dispatcher = new EventDispatcher();
        engine = new PhysicsManager(objects, dispatcher, isDebugMode);

        // Resizing
        Bounds bounds = canvas.getLayoutBounds();
        ChangeListener<Number> resize = (observable, oldValue, newValue) -> {
            double scale = Math.min(root.getWidth() / bounds.getWidth(), root.getHeight() / bounds.getHeight());
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);

        };

        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);
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

        stage.setScene(scene);
        stage.setTitle(isDebugMode ? "Debugger" : "Breakout");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.centerOnScreen();
        stage.show();

        timer.start();
        engine.start();
    }

    public void update() {
        engine.update();
        gui.update();
    }

    public void stop() {
        engine.stop();
    }

    public void pause() {
        engine.pause();
    }

    public void resume() {
        engine.resume();
    }

    @Override
    public void handle(Event event) {
        dispatcher.receiveEvent(event);
    }

    private double determineCanvasScaling(double worldWidth, double worldHeight) {
        double preferredScaling = 1.0;

        List<Screen> screens = List.of(Screen.getPrimary());

        for (Screen screen : screens) {
            // Visual bounds is the usable screen space which does not include the space start menu occupies.
            Rectangle2D visualBounds = screen.getVisualBounds();

            double sw = visualBounds.getWidth();
            double sh = visualBounds.getHeight() - 96;

            double scaling = Math.min(sw / worldWidth, sh / worldHeight);

            if (preferredScaling < scaling) {
                preferredScaling = scaling;
            }
        }

        return preferredScaling;
    }

}
