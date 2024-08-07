package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.engine.node.base.GameObject;
import com.github.berkbavas.breakout.engine.node.base.RectangularNode;
import lombok.Getter;

@Getter
public class Room extends RectangularNode implements GameObject {

    public Room(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
}
