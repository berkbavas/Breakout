package com.github.berkbavas.breakout.math;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

public abstract class AbstractPolygon2D<T extends LineSegment2D> {

    @Getter(AccessLevel.NONE)
    protected final List<T> edges;

    @Getter(AccessLevel.NONE)
    protected final List<Point2D> vertices;

    @Getter
    protected final int numberOfVertices;

    @Getter
    protected final int numberOfEdges;

    // Constructs a polygon in 2D Cartesian plane from counter-clockwise oriented vertices.
    public AbstractPolygon2D(List<Point2D> vertices, List<String> identifiers) {
        if (vertices.size() <= 2) {
            throw new RuntimeException("# of vertices must be at least 3.");
        }

        if (identifiers == null) {
            identifiers = new ArrayList<>(vertices.size());
            for (int i = 0; i < vertices.size(); ++i) {
                identifiers.add("");
            }
        }

        if (vertices.size() != identifiers.size()) {
            throw new RuntimeException("# of vertices and # of identifiers are different.");
        }

        this.vertices = List.copyOf(vertices);
        this.numberOfVertices = vertices.size();
        this.edges = new ArrayList<>(vertices.size() + 1);

        for (int i = 0; i < numberOfVertices; ++i) {
            final String identifier = identifiers.get(i);
            final Point2D P = vertices.get(i);
            final Point2D Q = vertices.get((i + 1) % numberOfVertices);
            final T edge = createEdge(P, Q, identifier);
            edges.add(edge);
        }

        this.numberOfEdges = edges.size();
    }

    // Constructs a polygon in 2D Cartesian plane from counter-clockwise oriented vertices.
    public AbstractPolygon2D(List<Point2D> vertices) {
        this(vertices, null);
    }

    protected abstract T createEdge(Point2D P, Point2D Q, String identifier);

    public List<T> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Point2D> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public Set<Point2D> findIntersections(AbstractPolygon2D<T> other) {
        Set<Point2D> intersections = new HashSet<>();

        for (T edge : edges) {
            for (T otherEdge : other.edges) {
                edge.findIntersection(otherEdge).ifPresent(intersections::add);
            }
        }

        return intersections;
    }

    public Set<Point2D> findIntersections(LineSegment2D ls) {
        Set<Point2D> intersections = new HashSet<>();

        for (LineSegment2D edge : edges) {
            edge.findIntersection(ls).ifPresent(intersections::add);
        }

        return intersections;
    }

    public Set<Point2D> findIntersections(Ray2D ray) {
        Set<Point2D> intersections = new HashSet<>();

        for (LineSegment2D edge : edges) {
            ray.findIntersection(edge).ifPresent(intersections::add);
        }

        return intersections;
    }
}
