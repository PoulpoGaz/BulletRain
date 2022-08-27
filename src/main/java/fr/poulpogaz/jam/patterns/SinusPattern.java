package fr.poulpogaz.jam.patterns;

import org.joml.Vector2f;

public class SinusPattern implements MovePattern {

    private final Vector2f dir;
    private final float radius;

    public SinusPattern(Vector2f dir, float radius) {
        this.dir = dir;
        this.radius = radius;
    }

    @Override
    public Vector2f dir(int t) {
        return null;
    }
}
