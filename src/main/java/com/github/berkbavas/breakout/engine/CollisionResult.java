package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.shapes.base.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CollisionResult {
    private StaticObject collidingObject;
    private Vector2D collisionNormal ;
}
