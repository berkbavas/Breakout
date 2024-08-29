package com.github.berkbavas.breakout.graphics;

import com.github.berkbavas.breakout.graphics.PaintCommandHandler.FillCommand;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler.PaintCommand;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler.StrokeCommand;
import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.base.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Painter {
    private final GraphicsContext gc;
    private final double width;
    private final double height;

    public Painter(GraphicsContext context, double width, double height) {
        this.gc = context;
        this.width = width;
        this.height = height;
    }

    public void scale(double scale) {
        gc.scale(scale, scale);
    }

    public void clear() {
        gc.clearRect(0, 0, width, height);
    }

    public void fillBackground(Color color) {
        gc.save();
        gc.setFill(color);
        gc.fillRect(0, 0, width, height);
        gc.restore();
    }

    public void save() {
        gc.save();
    }

    public void restore() {
        gc.restore();
    }

    public void drawLine(Point2D p0, Point2D p1, Color color, double thickness) {
        gc.setStroke(color);
        gc.setLineWidth(thickness);
        gc.strokeLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
    }

    public void drawLine(LineSegment2D ls, Color color, double thickness) {
        drawLine(ls.getP(), ls.getQ(), color, thickness);
    }

    public void drawLine(LineSegment2D ls, Color color) {
        drawLine(ls.getP(), ls.getQ(), color, 1.0);
    }

    public void stroke(DrawableLineSegment ls, Color color, double width) {
        drawLine(ls.getP(), ls.getQ(), color, width);
    }

    public void stroke(DrawableLineSegment ls, Color color) {
        stroke(ls, color, 1.0);
    }

    public void stroke(DrawableLineSegment ls) {
        stroke(ls, ls.getColor(), 1.0);
    }

    public void fillCircle(Point2D center, double radius, Color color) {
        gc.setFill(color);
        double left = center.getX() - radius;
        double top = center.getY() - radius;
        gc.fillOval(left, top, 2 * radius, 2 * radius);
    }

    public void fillCircle(Circle circle, Color color) {
        fillCircle(circle.getCenter(), circle.getRadius(), color);
    }

    public void fill(DrawableCircle circle, Color color) {
        fillCircle(circle.getCenter(), circle.getRadius(), color);
    }

    public void fill(DrawableCircle circle) {
        fillCircle(circle.getCenter(), circle.getRadius(), circle.getColor());
    }

    public void strokeCircle(Point2D center, double radius, Color color, double width) {
        gc.setStroke(color);
        gc.setLineWidth(width);
        double left = center.getX() - radius;
        double top = center.getY() - radius;
        gc.strokeOval(left, top, 2 * radius, 2 * radius);
    }

    public void strokeCircle(Circle circle, Color color, double width) {
        strokeCircle(circle.getCenter(), circle.getRadius(), color, width);
    }

    public void stroke(DrawableCircle circle, Color color, double width) {
        strokeCircle(circle, color, width);
    }

    public void stroke(DrawableCircle circle, Color color) {
        strokeCircle(circle, color, 1.0);
    }

    public void stroke(DrawableCircle circle) {
        strokeCircle(circle, circle.getColor(), 1.0);
    }

    public void fillRectangle(double x, double y, double w, double h, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, w, h);
    }

    public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRectangle(RectangularNode rect, double arcWidth, double arcHeight) {
        fillRoundRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), arcWidth, arcHeight, rect.getColor());
    }

    public void strokeRectangle(double x, double y, double w, double h, Color color, double width) {
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeRect(x, y, w, h);
    }

    public void fillPolygon(List<Point2D> vertices, Color color) {
        gc.setFill(color);
        gc.beginPath();
        Point2D first = vertices.get(0);
        gc.moveTo(first.getX(), first.getY());

        for (int i = 1; i < vertices.size(); ++i) {
            Point2D vertex = vertices.get(i);
            gc.lineTo(vertex.getX(), vertex.getY());
        }

        gc.closePath();
        gc.fill();
    }

    public void fill(PolygonalNode polygon, Color color) {
        fillPolygon(polygon.getVertices(), color);
    }

    public void fill(PolygonalNode polygon) {
        fillPolygon(polygon.getVertices(), polygon.getColor());
    }

    public void fill(RectangularNode rect, Color color) {
        fillPolygon(rect.getVertices(), color);
    }

    public void fill(RectangularNode rect) {
        fillPolygon(rect.getVertices(), rect.getColor());
    }

    public void strokePolygon(List<Point2D> vertices, Color color, double width) {
        gc.setStroke(color);
        gc.setLineWidth(width);

        gc.beginPath();
        Point2D first = vertices.get(0);
        gc.moveTo(first.getX(), first.getY());

        for (int i = 1; i < vertices.size(); ++i) {
            Point2D vertex = vertices.get(i);
            gc.lineTo(vertex.getX(), vertex.getY());
        }

        gc.closePath();
        gc.stroke();
    }

    public void stroke(PolygonalNode polygon, Color color, double width) {
        strokePolygon(polygon.getVertices(), color, width);
    }

    public void stroke(PolygonalNode polygon, Color color) {
        strokePolygon(polygon.getVertices(), color, 1.0);
    }

    public void stroke(PolygonalNode polygon) {
        strokePolygon(polygon.getVertices(), polygon.getColor(), 1.0);
    }

    public void stroke(RectangularNode rect, Color color, double width) {
        strokePolygon(rect.getVertices(), color, width);
    }

    public void stroke(RectangularNode rect, Color color) {
        strokePolygon(rect.getVertices(), color, 1.0);
    }

    public void stroke(RectangularNode rect) {
        strokePolygon(rect.getVertices(), rect.getColor(), 1.0);
    }

    public void processCommands(PaintCommandHandler handler) {
        List<PaintCommand> commands = handler.getCommands();

        for (PaintCommand command : commands) {
            Color color = command.getColor();
            Drawable shape = command.getShape();

            if (command instanceof FillCommand) {
                shape.fill(this, color);
            } else if (command instanceof StrokeCommand) {
                shape.stroke(this, color, ((StrokeCommand) command).getWidth());
            }
        }
    }
}
