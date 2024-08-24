package com.github.berkbavas.breakout.graphics;

import com.github.berkbavas.breakout.graphics.PaintCommandHandler.FillCommand;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler.PaintCommand;
import com.github.berkbavas.breakout.graphics.PaintCommandHandler.StrokeCommand;
import com.github.berkbavas.breakout.math.*;
import com.github.berkbavas.breakout.physics.node.Ball;
import com.github.berkbavas.breakout.physics.node.PolygonalNode;
import com.github.berkbavas.breakout.physics.node.RectangularNode;
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

    public void fillCircle(Point2D center, double radius, Color color) {
        gc.setFill(color);
        double left = center.getX() - radius;
        double top = center.getY() - radius;
        gc.fillOval(left, top, 2 * radius, 2 * radius);
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

    public void strokeCircle(Ball ball, double width) {
        strokeCircle(ball, ball.getColor(), width);
    }

    public void fillCircle(Circle circle, Color color) {
        fillCircle(circle.getCenter(), circle.getRadius(), color);
    }

    public void fillCircle(Ball ball) {
        fillCircle(ball.getCenter(), ball.getRadius(), ball.getColor());
    }

    public void fillRectangle(double x, double y, double w, double h, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, w, h);
    }

    public void fillRectangle(RectangularNode rect) {
        fillRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), rect.getColor());
    }

    public void fillRectangle(Rectangle2D rect, Color color) {
        fillRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color);
    }

    public void strokeRectangle(double x, double y, double w, double h, Color color, double width) {
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeRect(x, y, w, h);
    }

    public void strokeRectangle(RectangularNode rect, double width) {
        strokeRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), rect.getColor(), width);
    }

    public void strokeRectangle(Rectangle2D rect, Color color, double width) {
        strokeRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color, width);
    }

    public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRectangle(RectangularNode rect, double arcWidth, double arcHeight) {
        fillRoundRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), arcWidth, arcHeight, rect.getColor());
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


    public void fillPolygon(PolygonalNode polygon) {
        fillPolygon(polygon.getVertices(), polygon.getColor());
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

    public void strokePolygon(PolygonalNode polygon, double width) {
        strokePolygon(polygon.getVertices(), polygon.getColor(), width);
    }

    public void processCommands(PaintCommandHandler handler) {
        List<PaintCommand> commands = handler.getCommands();

        for (PaintCommand command : commands) {
            gc.save();

            Color color = command.getColor();
            Object shape = command.getShape();

            if (command instanceof FillCommand) {

                if (shape instanceof Circle) {
                    Circle rect = (Circle) shape;
                    fillCircle(rect, color);
                } else if (shape instanceof Rectangle2D) {
                    Rectangle2D rect = (Rectangle2D) shape;
                    fillRectangle(rect, color);
                } else if (shape instanceof Polygon2D) {
                    Polygon2D polygon = (Polygon2D) shape;
                    fillPolygon(polygon.getVertices(), color);
                }

            } else if (command instanceof StrokeCommand) {
                StrokeCommand strokeCommand = (StrokeCommand) command;
                double width = strokeCommand.getWidth();

                if (shape instanceof LineSegment2D) {
                    LineSegment2D ls = (LineSegment2D) shape;
                    drawLine(ls, color, width);
                } else if (shape instanceof Circle) {
                    Circle circle = (Circle) shape;
                    strokeCircle(circle, color, width);
                } else if (shape instanceof Rectangle2D) {
                    Rectangle2D rect = (Rectangle2D) shape;
                    strokeRectangle(rect, color, width);
                } else if (shape instanceof RectangularNode) {
                    RectangularNode rect = (RectangularNode) shape;
                    strokeRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color, width);
                } else if (shape instanceof Polygon2D) {
                    Polygon2D polygon = (Polygon2D) shape;
                    strokePolygon(polygon.getVertices(), color, width);
                } else if (shape instanceof PolygonalNode) {
                    PolygonalNode polygon = (PolygonalNode) shape;
                    strokePolygon(polygon.getVertices(), color, width);
                }

            }

            gc.restore();
        }

    }
}
