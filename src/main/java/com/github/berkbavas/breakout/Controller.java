package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.event.BreakoutEventDispatcher;
import com.github.berkbavas.breakout.event.DebuggerEventDispatcher;
import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.graphics.GraphicsEngine;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.PhysicsEngine;
import com.github.berkbavas.breakout.util.GameObjectConstructor;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;

public class Controller implements EventHandler<MouseEvent> {
    private static final HashMap<javafx.event.EventType<MouseEvent>, EventType> EVENT_CONVERSION_TABLE = new HashMap<>();

    static {
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_PRESSED, EventType.MOUSE_PRESSED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_CLICKED, EventType.MOUSE_CLICKED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_MOVED, EventType.MOUSE_MOVED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_DRAGGED, EventType.MOUSE_DRAGGED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_RELEASED, EventType.MOUSE_RELEASED);
    }

    private final PhysicsEngine engine;
    private final GraphicsEngine gui;
    private final GameObjects objects;
    private final EventDispatcher eventDispatcher;
    private final Scene scene;
    private final boolean isDebugMode;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public Controller(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;

        Group root = new Group();

        scene = new Scene(root, Color.BLACK);
        objects = GameObjectConstructor.construct(isDebugMode);

        if (isDebugMode) {
            eventDispatcher = new DebuggerEventDispatcher(objects.getDraggables());
        } else {
            eventDispatcher = new BreakoutEventDispatcher(objects.getPaddle());
        }

        gui = new GraphicsEngine(objects);
        OnDemandPaintCommandProcessor.initialize(gui);

        engine = new PhysicsEngine(objects, eventDispatcher, isDebugMode);

        eventDispatcher.addEventListener(engine);
        eventDispatcher.addEventListener(gui);

        root.getChildren().add(gui.getContainer());
        root.addEventHandler(MouseEvent.ANY, this);
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

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(isDebugMode ? "Debugger" : "Breakout");
        stage.sizeToScene();
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
    public void handle(MouseEvent event) {
        final Point2D position = transformToWorldCoordinates(event.getSceneX(), event.getSceneY());
        final EventType type = EVENT_CONVERSION_TABLE.getOrDefault(event.getEventType(), EventType.UNDEFINED);

        eventDispatcher.receiveEvent(type, position);
    }

    private Point2D transformToWorldCoordinates(double x, double y) {
        final double nx = x / gui.getWidth();
        final double ny = y / gui.getHeight();
        final double w = objects.getWorld().getWidth();
        final double h = objects.getWorld().getHeight();

        return new Point2D(nx * w, ny * h);
    }
}
