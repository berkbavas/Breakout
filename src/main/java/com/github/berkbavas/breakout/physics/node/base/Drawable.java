package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.gui.Painter;
import javafx.scene.paint.Color;

public interface Drawable {

    Color getColor();

    boolean isActiveDrawable();

    void setIsActiveDrawable(boolean isActiveDrawable);

    void stroke(Painter painter, Color color, double width);

    void stroke(Painter painter, Color color);

    void stroke(Painter painter);

    void fill(Painter painter, Color color);

    void fill(Painter painter);

}
