module com.github.berkbavas.breakout {
    requires javafx.controls;
    requires static lombok;
    requires jdk.jshell;
    exports com.github.berkbavas.breakout;
    exports com.github.berkbavas.breakout.math;
    exports com.github.berkbavas.breakout.engine.node;
}