package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.engine.node.base.GameObject;
import com.github.berkbavas.breakout.engine.node.base.RectangularNode;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class Brick extends RectangularNode implements GameObject {
    private final Color color;

    public Brick(float x, float y, float width, float height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }
}
