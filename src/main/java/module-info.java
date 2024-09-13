module com.github.berkbavas.breakout {
    requires javafx.controls;
    requires static lombok;
    requires jdk.jshell;
    requires java.desktop;
    requires imgui.binding;
    requires imgui.app;
    exports com.github.berkbavas.breakout;
    exports com.github.berkbavas.breakout.math;
    exports com.github.berkbavas.breakout.physics.node;
    exports com.github.berkbavas.breakout.event;
    exports com.github.berkbavas.breakout.gui;
    exports com.github.berkbavas.breakout.util;
    exports com.github.berkbavas.breakout.physics.handler;
    exports com.github.berkbavas.breakout.physics.simulator;
    exports com.github.berkbavas.breakout.physics.node.base;
    exports com.github.berkbavas.breakout.physics.simulator.processor;
    exports com.github.berkbavas.breakout.physics.simulator.collision;
    exports com.github.berkbavas.breakout.physics.simulator.processor.resolver;
    exports com.github.berkbavas.breakout.physics.simulator.helper;
    exports com.github.berkbavas.breakout.core;

}