package com.github.berkbavas.breakout.engine.node.base;

import com.github.berkbavas.breakout.math.Point2D;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class RectangularNode extends StaticNode {

    private final Edge left;
    private final Edge right;
    private final Edge top;
    private final Edge bottom;

    private final float width;
    private final float height;

    private final Vertex leftTop;
    private final Vertex leftBottom;
    private final Vertex rightTop;
    private final Vertex rightBottom;

    private final ArrayList<Edge> edges = new ArrayList<>();
    private final ArrayList<Vertex> vertices = new ArrayList<>();

    public RectangularNode(float x, float y, float width, float height) {

        this.leftTop = new Vertex(x, y, Vertex.Type.LEFT_TOP);
        this.leftBottom = new Vertex(x, y + height, Vertex.Type.LEFT_BOTTOM);
        this.rightTop = new Vertex(x + width, y, Vertex.Type.RIGHT_TOP);
        this.rightBottom = new Vertex(x + width, y + height, Vertex.Type.RIGHT_BOTTOM);

        this.left = new Edge(leftTop, leftBottom);
        this.right = new Edge(rightTop, rightBottom);
        this.top = new Edge(leftTop, rightTop);
        this.bottom = new Edge(leftBottom, rightBottom);

        edges.add(left);
        edges.add(right);
        edges.add(top);
        edges.add(bottom);

        vertices.add(leftTop);
        vertices.add(leftBottom);
        vertices.add(rightTop);
        vertices.add(rightBottom);

        this.width = width;
        this.height = height;
    }

    @Override
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    @Override
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public boolean contains(Point2D point) {
        final float xMin = leftTop.getX();
        final float xMax = leftTop.getX() + width;
        final float yMin = leftTop.getY();
        final float yMax = leftTop.getY() + height;

        final float x = point.getX();
        final float y = point.getY();

        boolean xBound = xMin <= x && x <= xMax;
        boolean yBound = yMin <= y && y <= yMax;

        return xBound && yBound;
    }
}
