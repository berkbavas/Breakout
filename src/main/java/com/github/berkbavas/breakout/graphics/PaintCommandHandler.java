package com.github.berkbavas.breakout.graphics;

import com.github.berkbavas.breakout.math.Circle;
import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.math.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PaintCommandHandler {

    private final ConcurrentLinkedQueue<PaintCommand> commands = new ConcurrentLinkedQueue<>();

    public void clear() {
        commands.clear();
    }

    public void fillRectangle(Rectangle2D rect, Color color) {
        commands.add(new FillCommand(rect, color));
    }

    public void strokeRectangle(Rectangle2D rect, Color color, double width) {
        commands.add(new StrokeCommand(rect, color, width));
    }

    public void drawLine(LineSegment2D ls, Color color) {
        commands.add(new StrokeCommand(ls, color, 1));
    }

    public void drawLine(LineSegment2D ls, Color color, double width) {
        commands.add(new StrokeCommand(ls, color, width));
    }

    public void drawLine(Point2D p0, Point2D p1, Color color, double width) {
        drawLine(new LineSegment2D(p0, p1), color, width);
    }

    public void drawLine(Point2D p0, Point2D p1, Color color) {
        drawLine(new LineSegment2D(p0, p1), color);
    }

    public void fillCircle(Circle circle, Color color) {
        commands.add(new FillCommand(circle, color));
    }

    public void strokeCircle(Circle circle, Color color, double width) {
        commands.add(new StrokeCommand(circle, color, width));
    }

    public void strokeCircle(Circle circle, Color color) {
        commands.add(new StrokeCommand(circle, color, 1));
    }

    public List<PaintCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    @Getter
    public static class PaintCommand {
        private final Object shape;
        private final Color color;

        PaintCommand(Object shape, Color color) {
            this.shape = shape;
            this.color = color;
        }
    }

    public static class FillCommand extends PaintCommand {
        FillCommand(Object shape, Color color) {
            super(shape, color);
        }
    }

    @Getter
    public static class StrokeCommand extends PaintCommand {
        private final double width;

        StrokeCommand(Object shape, Color color, double width) {
            super(shape, color);
            this.width = width;
        }
    }
}
