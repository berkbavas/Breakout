package com.github.berkbavas.breakout.engine.node.base;

import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Vector2D;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class Edge extends LineSegment2D {

    public enum NormalOrientation {
        INWARDS,
        OUTWARDS
    }

    private final HashMap<NormalOrientation, Vector2D> normals = new HashMap<>();

    public Edge(Point2D P, Point2D Q) {
        super(P, Q);
        constructNormals();
    }

    public Vector2D getNormal(NormalOrientation normalOrientation) {
        return normals.get(normalOrientation);
    }

    private void constructNormals() {
        final float dx = getQ().getX() - getP().getX();
        final float dy = getQ().getY() - getP().getY();

        normals.put(NormalOrientation.INWARDS, new Vector2D(-dy, dx).normalized());
        normals.put(NormalOrientation.OUTWARDS, new Vector2D(dy, -dx).normalized());
    }
}
