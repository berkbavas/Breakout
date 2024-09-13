package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.core.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Debugger extends Application {

    private final Controller controller;

    public Debugger() {
        controller = new Controller(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        controller.start(primaryStage);
    }

    @Override
    public void stop() {
        controller.stop();
    }
}
