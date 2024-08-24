package com.github.berkbavas.breakout;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Debugger extends Application {
    static {
        Font.loadFont(Objects.requireNonNull(Breakout.class.getResource("/font/BlackOpsOne-Regular.ttf")).toExternalForm(), 0);
    }

    private final Controller controller;

    public static void main(String[] args) {
        System.out.println(":::.. Debugger ..:::");
        launch(args);
    }

    public Debugger() {
        controller = new Controller(true);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Debugger.start()");
        controller.start(primaryStage);
    }

    @Override
    public void stop() {
        System.out.println("Debugger.stop()");
        controller.stop();
    }
}
