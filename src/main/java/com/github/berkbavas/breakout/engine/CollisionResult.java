package com.github.berkbavas.breakout.engine;

import com.github.berkbavas.breakout.engine.node.Ball;
import com.github.berkbavas.breakout.engine.node.base.StaticNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CollisionResult {
    private final StaticNode collider;
    private final Ball newBall;
    private final float timeToCollision;
}
