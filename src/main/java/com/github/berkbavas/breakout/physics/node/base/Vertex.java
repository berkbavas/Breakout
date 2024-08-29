package com.github.berkbavas.breakout.physics.node.base;

import com.github.berkbavas.breakout.math.Point2D;
import javafx.util.Pair;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Vertex extends Point2D {
    private final ColliderEdge owner;

    public Vertex(ColliderEdge owner, double x, double y) {
        super(x, y);
        this.owner = owner;
    }

    public Vertex(ColliderEdge owner, Point2D point) {
        super(point.getX(), point.getY());
        this.owner = owner;
    }

    public static Pair<Point2D, Point2D> findClosestPair(List<Pair<Point2D, Vertex>> listOfPairs) {
        Set<Pair<Point2D, Point2D>> converted = new HashSet<>();

        for (Pair<Point2D, Vertex> pair : listOfPairs) {
            converted.add(new Pair<>(pair.getKey(), pair.getValue()));
        }

        return Point2D.findClosestPair(converted);
    }
}
