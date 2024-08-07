package com.github.berkbavas.breakout.engine.node.base;

import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;

import java.util.HashMap;

public class Vertex extends Point2D {

    public enum Type {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTER,
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }

    public static final HashMap<Type, Vector2D> NORMALS = new HashMap<>();

    static {
        NORMALS.put(Type.LEFT, new Vector2D(-1, 0));
        NORMALS.put(Type.RIGHT, new Vector2D(1, 0));
        NORMALS.put(Type.TOP, new Vector2D(0, -1));
        NORMALS.put(Type.BOTTOM, new Vector2D(0, 1));
        NORMALS.put(Type.CENTER, new Vector2D(0, 0));
        NORMALS.put(Type.LEFT_TOP, new Vector2D(-1, -1).normalized());
        NORMALS.put(Type.RIGHT_TOP, new Vector2D(1, -1).normalized());
        NORMALS.put(Type.LEFT_BOTTOM, new Vector2D(-1, 1).normalized());
        NORMALS.put(Type.RIGHT_BOTTOM, new Vector2D(1, 1).normalized());
    }

    private final Type type;

    public Vertex(float x, float y, Type type) {
        super(x, y);
        this.type = type;
    }

    public Vector2D getNormal() {
        return NORMALS.get(type);
    }
}
