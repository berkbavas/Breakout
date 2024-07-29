package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.shapes.base.Disk;

import java.util.Optional;

public interface StaticObject {

    Optional<CollisionResult> checkCollision(Disk ball);

    void processCollision(Disk ball, CollisionResult result, float ifps);
}
