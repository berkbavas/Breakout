module com.github.berkbavas.breakout {
    requires javafx.controls;
    requires static lombok;
    exports com.github.berkbavas.breakout to javafx.controls, javafx.graphics;
    exports com.github.berkbavas.breakout.shapes.complex to javafx.controls, javafx.graphics;
    exports com.github.berkbavas.breakout.engine to javafx.controls, javafx.graphics;
}