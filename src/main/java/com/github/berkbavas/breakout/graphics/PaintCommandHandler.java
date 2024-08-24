package com.github.berkbavas.breakout.graphics;

import com.github.berkbavas.breakout.math.LineSegment2D;
import com.github.berkbavas.breakout.math.Point2D;
import com.github.berkbavas.breakout.physics.node.Drawable;
import com.github.berkbavas.breakout.physics.node.DrawableLineSegment;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PaintCommandHandler {

    private final ConcurrentLinkedQueue<PaintCommand> commands = new ConcurrentLinkedQueue<>();

    public void clear() {
        commands.clear();
    }

    public void drawLine(Point2D p0, Point2D p1, Color color, double width) {
        commands.add(new StrokeCommand(new DrawableLineSegment(p0, p1, color), color, width));
    }

    public void drawLine(Point2D p0, Point2D p1, Color color) {
        commands.add(new StrokeCommand(new DrawableLineSegment(p0, p1, color), color, 1.0));
    }

    public void drawLine(LineSegment2D ls, Color color, double width) {
        commands.add(new StrokeCommand(new DrawableLineSegment(ls.getP(), ls.getQ(), color), color, width));
    }

    public void drawLine(LineSegment2D ls, Color color) {
        commands.add(new StrokeCommand(new DrawableLineSegment(ls.getP(), ls.getQ(), color), color, 1.0));
    }

    public void drawLine(DrawableLineSegment ls, Color color) {
        commands.add(new StrokeCommand(ls, color, 1.0));
    }

    public void drawLine(DrawableLineSegment ls) {
        commands.add(new StrokeCommand(ls, ls.getColor(), 1.0));
    }

    public void stroke(Drawable drawable, Color color, double width) {
        commands.add(new StrokeCommand(drawable, color, width));
    }

    public void stroke(Drawable drawable, Color color) {
        commands.add(new StrokeCommand(drawable, color, 1.0));
    }

    public void stroke(Drawable drawable) {
        commands.add(new StrokeCommand(drawable, drawable.getColor(), 1.0));
    }

    public void fill(Drawable drawable, Color color) {
        commands.add(new FillCommand(drawable, color));
    }

    public void fill(Drawable drawable) {
        commands.add(new FillCommand(drawable, drawable.getColor()));
    }

    public List<PaintCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    @Getter
    @AllArgsConstructor
    public static abstract class PaintCommand {
        private final Drawable shape;
        private final Color color;
    }

    public static class FillCommand extends PaintCommand {
        FillCommand(Drawable shape, Color color) {
            super(shape, color);
        }
    }

    @Getter
    public static class StrokeCommand extends PaintCommand {
        private final double width;

        StrokeCommand(Drawable shape, Color color, double width) {
            super(shape, color);
            this.width = width;
        }
    }
}
