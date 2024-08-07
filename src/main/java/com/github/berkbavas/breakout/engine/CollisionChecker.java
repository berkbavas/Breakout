package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.Brick;
import com.github.berkbavas.breakout.engine.node.Paddle;
import com.github.berkbavas.breakout.engine.node.Room;
import com.github.berkbavas.breakout.engine.node.base.BoundingBox;
import com.github.berkbavas.breakout.engine.node.base.Edge;
import com.github.berkbavas.breakout.engine.node.base.RectangularNode;
import com.github.berkbavas.breakout.engine.node.base.Vertex;
import com.github.berkbavas.breakout.math.*;

import java.util.*;

public final class CollisionChecker {

    private CollisionChecker() {
    }

    // 1) Check if ball is already colliding with the node.
    //    This is an artifact of the discrete collision detection.
    //    If this is the case, then rollback to previous position and reflect the velocity.
    //
    // 2) Check collision with vertices.
    //    Will there be a collision between vertices and the ball in the given delta time?
    //    To answer this question we simply check if any of these four vertices is contained
    //    in the ball in the given delta time.
    //
    // 3) Check collision with edges.
    //    Similar logic as for the vertices, but this time we check collision between edges and the ball.
    //
    //
    //   LEFT_TOP            RIGHT_TOP
    //         *-------------*           <--- () Ball
    //         |             |
    //         |    Node     |      |-----?----|
    //         |             |          Distance to go in delta time
    //         *-------------*
    //   LEFT_BOTTOM         RIGHT_BOTTOM
    //                                      \
    // * : Vertex                            \
    // - : Edge                               \
    // (): Ball                                () Ball
    //

    public static ArrayList<CollisionResult> checkCollision(RectangularNode node, Ball currentBall, Ball previousBall, float deltaTime) {
        ArrayList<CollisionResult> list = new ArrayList<>();

        if(!(node instanceof Room)){
            checkIfBallAlreadyColliding(node, currentBall, previousBall, deltaTime).ifPresent(list::add);

            if (!list.isEmpty()) {
                return list;
            }
        }

        checkCollisionWithVertices(node, currentBall, deltaTime).ifPresent(list::add);
        checkCollisionWithEdges(node, currentBall, deltaTime).ifPresent(list::add);

        return list;
    }

    private static Optional<CollisionResult> checkIfBallAlreadyColliding(RectangularNode node, Ball currentBall, Ball previousBall, float deltaTime) {
        final BoundingBox box = currentBall.getBoundingBox();
        final Collection<Point2D> vertices = box.getVertices();
        boolean colliding = false;
        for (Point2D vertex : vertices) {
            if (node.contains(vertex)) {
                colliding = true;
                break;
            }
        }

        if (!colliding) {
            return Optional.empty();
        }

        final Point2D center = previousBall.getCenter();
        final float radius = previousBall.getRadius();
        final Vector2D velocity = currentBall.getVelocity().multiply(-1.0f);

        Ball newBall = new Ball(center, radius, velocity).move(deltaTime);
        CollisionResult collisionResult = new CollisionResult(node, newBall, deltaTime);

        return Optional.of(collisionResult);
    }

    private static Optional<CollisionResult> checkCollisionWithVertices(RectangularNode node, Ball ball, float deltaTime) {

        final List<Vertex> vertices = node.getVertices();
        final Vector2D velocity = ball.getVelocity();
        final Point2D center = ball.getCenter();
        final float radius = ball.getRadius();

        float minDistance = Float.MAX_VALUE;
        Vertex colliderVertex = null;
        float timeToCollision = 0.0f;


        for (Vertex vertex : vertices) {
            // Is the ball going towards the vertex?
            // If it is going away from the vertex, then discard this vertex.
            // Because it is not subject to a possible collision.

            final Vector2D centerToVertex = center.subtract(vertex).toVector2D();
            final float dot = centerToVertex.dot(velocity);
            final boolean goingAway = Util.isLessThanOrEqualToZero(dot);
            if (goingAway) {
                continue;
            }

            // If we are here, then it means that the ball going towards to the vertex.

            // First check if we are already colliding with the vertex.
            // This case is possible but not too much probable.

            final boolean colliding = ball.contains(vertex);
            if (colliding) {
                // If we are already colliding with a vertex,
                // then we must process this case as soon as possible.
                // Velocity of the ball must be reflected along the vertex normal
                // and ball must move as delta distance.

                colliderVertex = vertex;
                minDistance = 0.0f;
                break; // Break this for-loop, this vertex has priority.
            }

            // Let's check if a collision will occur in the given delta time.
            final float scalar = dot / centerToVertex.l2orm();
            final Vector2D projection = centerToVertex.multiply(scalar);
            final float speed = projection.norm();
            final float distanceToGo = speed * deltaTime;
            final float distanceToCenter = center.distanceTo(vertex);
            final float criticalDistance = distanceToCenter - radius;

            // If the ball moves more than the critical distance
            // then a collision will occur.
            final boolean collides = distanceToGo >= criticalDistance;
            if (!collides) {
                continue;
            }

            // Collision!
            // If we are here then it means that there will be a collision.
            // The logic below seeks for the earliest (or the closest one).

            if (criticalDistance < minDistance) {
                // Among all the distances choose the minimum
                colliderVertex = vertex;
                minDistance = criticalDistance;
                timeToCollision = criticalDistance / speed;
            }
        }

        if (colliderVertex == null) {
            // Well, it seems there is no collision!
            // We are good to go.
            return Optional.empty();
        }

        // Hmm, there is a collision.
        // Let's do some math for the collision impact.

        CollisionResult collisionResult;
        final Vector2D collisionNormal = colliderVertex.getNormal();

        if (Util.isFuzzyZero(minDistance)) {
            // We are already colliding with the vertex.
            // So reflect the velocity and move as mush as the delta time permits.
            Ball newBall = ball.collideThenMove(collisionNormal, deltaTime);
            collisionResult = new CollisionResult(node, newBall, deltaTime);

        } else {
            Ball newBall = ball.moveThenCollide(collisionNormal, timeToCollision);
            collisionResult = new CollisionResult(node, newBall, deltaTime);
        }

        return Optional.of(collisionResult);
    }

    private static Optional<CollisionResult> checkCollisionWithEdges(RectangularNode node, Ball ball, float deltaTime) {
        // There might be several collision, so we will find the most probable.
        final List<Edge> edges = node.getEdges();

        float minTime = Float.MAX_VALUE;
        Edge colliderEdge = null;

        for (Edge edge : edges) {
            Optional<Float> maybe = CollisionChecker.calculateTimeToCollision(edge, ball, deltaTime);
            if (maybe.isPresent()) {
                final float timeToCollision = maybe.get();
                if (timeToCollision < minTime) {
                    minTime = timeToCollision;
                    colliderEdge = edge;
                }
            }
        }

        if (colliderEdge == null) {
            return Optional.empty();
        }

        Vector2D collisionNormal;

        if (node instanceof Brick || node instanceof Paddle) {
            collisionNormal = colliderEdge.getNormal(Edge.NormalOrientation.OUTWARDS);
        } else if (node instanceof Room) {
            collisionNormal = colliderEdge.getNormal(Edge.NormalOrientation.INWARDS);
        } else {
            throw new RuntimeException("Undefined node type!");
        }

        Ball newBall;

        if (Util.isFuzzyZero(minTime)) {
            minTime = deltaTime;
            newBall = ball.collideThenMove(collisionNormal, deltaTime);
        } else {
            newBall = ball.moveThenCollide(collisionNormal, minTime);
        }

        CollisionResult collisionResult = new CollisionResult(node, newBall, minTime);
        return Optional.of(collisionResult);
    }

    // Returns how much time will take until the collision happens.
    // Returns empty if there is no collision in the given delta time.
    // Returns zero if the objects are already colliding.
    private static Optional<Float> calculateTimeToCollision(LineSegment2D lineSegment, Ball ball, float deltaTime) {
        ArrayList<Float> distances = calculateDistancesIfIntersects(lineSegment, ball);

        final float speed = ball.getVelocity().norm();
        final float distanceToWillGo = speed * deltaTime;
        boolean collides = false;
        float maxDistanceToCanGo = Float.MAX_VALUE;

        for (float distance : distances) {

            if (distance > distanceToWillGo) {
                continue;
            }

            if (distance < maxDistanceToCanGo) {
                collides = true;
                maxDistanceToCanGo = distance;
            }
        }

        if (collides) {
            final float timeToBeConsumed = maxDistanceToCanGo / speed;
            return Optional.of(timeToBeConsumed);
        } else {
            return Optional.empty();
        }
    }

    // Returns distances between the ray origin and intersection points if any intersection exists.
    // Distance may be zero if objects are touching.
    private static ArrayList<Float> calculateDistancesIfIntersects(LineSegment2D lineSegment, Ball ball) {
        HashMap<Vertex.Type, Ray2D> rays = ball.getBoundingBox().getCriticalRays();
        ArrayList<Float> distances = new ArrayList<>();

        rays.forEach((Vertex.Type vertexType, Ray2D ray) -> {
            Optional<Point2D> intersection = lineSegment.findIntersection(ray);
            intersection.ifPresent((Point2D val) -> {
                final float distance = val.distanceTo(ray.getOrigin());
                distances.add(distance);
            });
        });

        return distances;
    }
}
