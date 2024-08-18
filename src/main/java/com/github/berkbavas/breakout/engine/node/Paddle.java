package com.github.berkbavas.breakout.engine.node;

import com.github.berkbavas.breakout.Constants;
import com.github.berkbavas.breakout.engine.CollisionDetector;
import com.github.berkbavas.breakout.math.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class Paddle extends Rectangle2D implements StaticNode {
    private final Color color;

    public Paddle(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public Paddle(Point2D leftTop, Point2D leftBottom, Point2D rightTop, Point2D rightBottom, Color color) {
        super(leftTop, leftBottom, rightTop, rightBottom);
        this.color = color;
    }

    // Returns a new paddle instance by taking care of a potential collision between circle, i.e.,
    // checks if a collision is potential along the path from current position to the new position and
    // makes sure that the returned paddle instance is not colliding with the circle.
    public Paddle getNewPaddleByTakingCareOfCollision(Point2D newTopLeftPosition, Circle circle) {
        List<Pair<Point2D, Point2D>> collisionContacts = new ArrayList<>();
        Set<LineSegment2D> edges = getEdges();
        Vector2D velocity = newTopLeftPosition.subtract(getLeftTop());
        edges.forEach((edge) -> CollisionDetector.findCollision(edge, velocity, circle).ifPresent(collisionContacts::add));

        // Find the closest pair among all collision contact pairs.
        Pair<Point2D, Point2D> earliestCollision = Point2D.findClosestPair(collisionContacts);

        if (earliestCollision != null) {
            // Collision will occur between paddle and circle along the direction of velocity.

            // Update the paddle by taking care of such collision.
            Point2D contactPointOnEdge = earliestCollision.getKey();
            Point2D contactPointOnCircle = earliestCollision.getValue();


            double xDeltaMaxPermitted = Math.abs(contactPointOnEdge.getX() - contactPointOnCircle.getX());
            boolean isPaddleAndCircleTooClose = xDeltaMaxPermitted < Constants.Paddle.DO_NOT_MOVE_PADDLE_IF_BALL_TOO_CLOSE_OFFSET;

            if (isPaddleAndCircleTooClose) {
                // Don't update.
                return this;
            }

            double xDeltaAskedFor = newTopLeftPosition.getX() - getLeftTop().getX();
            double xDelta = Util.clamp(-xDeltaMaxPermitted, xDeltaAskedFor, xDeltaMaxPermitted);
            double xClampedTopLeft = getLeftTop().getX() + xDelta;

            return constructFromNewTopLeftXPosition(xClampedTopLeft);
        }

        // No collision, just update to the new top left position.
        return constructFromNewTopLeftXPosition(newTopLeftPosition.getX());
    }

    private Paddle constructFromNewTopLeftXPosition(double x) {
        double y = getLeftTop().getY();
        double w = getWidth();
        double h = getHeight();
        Color c = getColor();
        return new Paddle(x, y, w, h, c);
    }

    @Override
    public double getCollisionImpactFactor() {
        return Constants.Paddle.COLLISION_IMPACT_FACTOR;
    }

}
