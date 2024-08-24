package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.event.EventDispatcher;
import com.github.berkbavas.breakout.graphics.GraphicsEngine;
import com.github.berkbavas.breakout.graphics.OnDemandPaintCommandProcessor;
import com.github.berkbavas.breakout.physics.PhysicsEngine;
import com.github.berkbavas.breakout.physics.node.World;
import com.github.berkbavas.breakout.util.GameObjectConstructor;
import com.github.berkbavas.breakout.util.TransformationHelper;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class Controller implements EventHandler<Event> {

    private final Scene scene;
    private final Group root;
    private final PhysicsEngine engine;
    private final GraphicsEngine ig;
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

        root = new Group();
        scene = new Scene(root, Color.rgb(244, 244, 244));
        objects = GameObjectConstructor.construct(isDebugMode);
        World world = objects.getWorld();
        double scaling = determineCanvasScaling(world.getWidth(), world.getHeight());

        ig = new GraphicsEngine(root, objects, scaling);

        OnDemandPaintCommandProcessor.initialize(ig);
        TransformationHelper.initialize(world.getWidth(), world.getHeight(), ig.getWidth(), ig.getHeight());

        dispatcher = new EventDispatcher();
        engine = new PhysicsEngine(objects, dispatcher, isDebugMode);

        dispatcher.addEventListener(ig);

        root.addEventHandler(Event.ANY, this);

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
        engine.start();
    }

    public void update() {
        engine.update();
        ig.update();
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
