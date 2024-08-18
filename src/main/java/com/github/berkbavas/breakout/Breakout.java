package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.engine.Engine;
import com.github.berkbavas.breakout.gui.ImageGenerator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Breakout extends Application implements EventHandler<MouseEvent> {
    static {
        Font.loadFont(Objects.requireNonNull(Breakout.class.getResource("/font/BlackOpsOne-Regular.ttf")).toExternalForm(), 0);
    }

    private final Robot robot = new Robot();
    private final StackPane root = new StackPane();
    private final Engine engine;
    private final ImageGenerator ig;

    private final GameObjects gameObjects;
    private final SharedState sharedState;

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    private boolean focus = false;

    public void update() {
        engine.update();
        ig.update();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Breakout() {
        gameObjects = GameObjectConstructor.construct();
        sharedState = new SharedState();
        engine = new Engine(gameObjects, sharedState);
        ig = new ImageGenerator(gameObjects, sharedState);
    }

    @Override
    public void start(Stage primaryStage) {

        root.getChildren().add(ig.getContainer());
        Scene scene = new Scene(root, Color.BLACK);

//        manager.getGameBoard().setOnMouseClicked(event -> {
//            focus = !focus;
//            scene.setCursor(focus ? Cursor.NONE : Cursor.DEFAULT);
//        });


//        Bounds bounds = controller.getContainer().getLayoutBounds();
//        primaryStage.setMinWidth(bounds.getWidth() / 1.25);
//        primaryStage.setMinHeight(bounds.getHeight() / 1.25);


        ig.getGameBoard().addEventHandler(MouseEvent.ANY, engine);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout");
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();

        timer.start();
        engine.start();
    }

    @Override
    public void stop() {
        System.out.println("Breakout.stop()");
        engine.stop();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

    }

}
