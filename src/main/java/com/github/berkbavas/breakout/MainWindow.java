package com.github.berkbavas.breakout;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindow extends Application {
    static {
        Font.loadFont(Objects.requireNonNull(MainWindow.class.getResource("/font/BlackOpsOne-Regular.ttf")).toExternalForm(), 0);
    }

    private final Robot robot = new Robot();
    private final StackPane root = new StackPane();
    private final Controller controller = new Controller();

    private boolean focus = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        root.getChildren().add(controller.getContainer());
        Scene scene = new Scene(root, Color.BLACK);

//        manager.getGameBoard().setOnMouseClicked(event -> {
//            focus = !focus;
//            scene.setCursor(focus ? Cursor.NONE : Cursor.DEFAULT);
//        });

//        scene.setOnMouseMoved(event -> {
//            if (focus) {
//                manager.movePaddle(event.getX() - scene.getWidth() * 0.5);
//                double x = scene.getWindow().getX() + scene.getWindow().getWidth() * 0.5;
//                double y = scene.getWindow().getY() + scene.getWindow().getHeight() * 0.5;
//                robot.mouseMove(x, y);
//            }
//        });

//        Bounds bounds = controller.getContainer().getLayoutBounds();
//        primaryStage.setMinWidth(bounds.getWidth() / 1.25);
//        primaryStage.setMinHeight(bounds.getHeight() / 1.25);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout");
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();

        controller.start();
    }

    @Override
    public void stop() {
        System.out.println("MainWindow.stop()");
    }
}
