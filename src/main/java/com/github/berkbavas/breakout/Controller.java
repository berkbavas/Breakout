package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.event.EventType;
import com.github.berkbavas.breakout.graphics.GraphicsEngine;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.PhysicsEngine;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.util.GameObjectConstructor;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

public class Controller implements EventHandler<MouseEvent> {
    private static final HashMap<javafx.event.EventType<MouseEvent>, EventType> EVENT_CONVERSION_TABLE = new HashMap<>();

    static {
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_PRESSED, EventType.MOUSE_PRESSED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_CLICKED, EventType.MOUSE_CLICKED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_MOVED, EventType.MOUSE_MOVED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_DRAGGED, EventType.MOUSE_DRAGGED);
        EVENT_CONVERSION_TABLE.put(MouseEvent.MOUSE_RELEASED, EventType.MOUSE_RELEASED);
    }

    private final Scene scene;
    private final PhysicsEngine physicsEngine;
    private final GraphicsEngine graphicsEngine;
    private final GameObjects objects;
    private final EventDispatcher eventDispatcher;

    private final boolean isDebugMode;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public Controller(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;

        StackPane root = new StackPane();

        scene = new Scene(root, Color.rgb(244, 244, 244));
        objects = GameObjectConstructor.construct(isDebugMode);

        World world = objects.getWorld();
        double scaling = determineCanvasScaling(world.getWidth(), world.getHeight());

        graphicsEngine = new GraphicsEngine(objects, scaling);
        OnDemandPaintCommandProcessor.initialize(graphicsEngine);

        eventDispatcher = new EventDispatcher();
        physicsEngine = new PhysicsEngine(objects, eventDispatcher, isDebugMode);

        eventDispatcher.addEventListener(graphicsEngine);

        Parent container = graphicsEngine.getContainer();

        root.getChildren().add(container);
        container.addEventHandler(MouseEvent.ANY, this);

//        Bounds bounds = container.getLayoutBounds();
//        ChangeListener<Number> resize = (observable, oldValue, newValue) -> {
//            double scaleX = root.getWidth() / bounds.getWidth();
//            double scaleY = root.getHeight() / bounds.getHeight();
//
//            double scale = Math.min(scaleX, scaleY);
//
//            container.setScaleX(scale);
//            container.setScaleY(scale);
//        };
//
//        root.widthProperty().addListener(resize);
//        root.heightProperty().addListener(resize);
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
        physicsEngine.start();
    }

    public void update() {
        physicsEngine.update();
        graphicsEngine.update();
    }

    public void stop() {
        physicsEngine.stop();
    }

    public void pause() {
        physicsEngine.pause();
    }

    public void resume() {
        physicsEngine.resume();
    }

    @Override
    public void handle(MouseEvent event) {
        final Point2D position = transformToWorldCoordinates(event.getX(), event.getY());
        final EventType type = EVENT_CONVERSION_TABLE.getOrDefault(event.getEventType(), EventType.UNDEFINED);

        eventDispatcher.receiveEvent(type, position);
    }

    private Point2D transformToWorldCoordinates(double x, double y) {
        final double nx = x / graphicsEngine.getWidth();
        final double ny = y / graphicsEngine.getHeight();
        final double w = objects.getWorld().getWidth();
        final double h = objects.getWorld().getHeight();

        return new Point2D(nx * w, ny * h);
    }

    private double determineCanvasScaling(double worldWidth, double worldHeight) {
        double preferredScaling = 1.0;

       List<Screen> screens =  List.of(Screen.getPrimary());

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
