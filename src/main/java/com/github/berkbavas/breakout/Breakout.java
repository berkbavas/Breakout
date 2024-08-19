package com.github.berkbavas.breakout;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Breakout extends Application {
    static {
        Font.loadFont(Objects.requireNonNull(Breakout.class.getResource("/font/BlackOpsOne-Regular.ttf")).toExternalForm(), 0);
    }

    private final Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    public Breakout() {
        controller = new Controller();
    }

    @Override
    public void start(Stage primaryStage) {
        controller.start(primaryStage);
    }

    @Override
    public void stop() {
        System.out.println("Breakout.stop()");
        controller.stop();
    }
}
