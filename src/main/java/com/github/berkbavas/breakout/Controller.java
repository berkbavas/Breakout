package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.Engine;
import com.github.berkbavas.breakout.engine.PaddleActionListener;
import com.github.berkbavas.breakout.gui.ImageGenerator;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller implements EventHandler<Event> {
    private final StackPane root;
    private final Engine engine;
    private final ImageGenerator gui;
    private final GameObjects gameObjects;
    private final SharedState sharedState;
    // private final Timer timer = new Timer("Controller", true);

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };


    public Controller() {
        root = new StackPane();
        gameObjects = GameObjectConstructor.construct();
        sharedState = new SharedState();
        engine = new Engine(gameObjects, sharedState);
        gui = new ImageGenerator(gameObjects, sharedState);
    }

    public void start(Stage stage) {

        root.getChildren().add(gui.getContainer());
        Scene scene = new Scene(root, Color.BLACK);

        gui.getGameBoard().addEventHandler(MouseEvent.ANY, this);

        stage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
            final boolean hidden = newVal;
            if (hidden) {
                pause();
            } else {
                resume();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Breakout");
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
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            handle((MouseEvent) event);
        }
    }

    private void handle(MouseEvent event) {
        Object o = event.getSource();

        if (o instanceof Canvas) {
            Canvas source = (Canvas) o;
            double x = event.getSceneX() / source.getWidth();
            double y = event.getSceneY() / source.getHeight();
            var eventType = event.getEventType();

            PaddleActionListener listener = engine.getPaddleActionListener();

            if (eventType == MouseEvent.MOUSE_PRESSED) {
                listener.onMousePressed(x, y);
            }

            if (eventType == MouseEvent.MOUSE_DRAGGED) {
                listener.onMouseDragged(x, y);
            }
        }
    }
}
