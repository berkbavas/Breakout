package com.github.berkbavas.breakout;

import com.github.berkbavas.breakout.core.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Breakout extends Application {

    private final Controller controller;

    public Breakout() {
        controller = new Controller(false);
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
