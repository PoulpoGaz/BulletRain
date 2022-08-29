package fr.poulpogaz.jam.patterns;

import org.joml.Vector2f;

public class LinearPattern implements MovePattern {

    private final Vector2f dir;

    public LinearPattern(Vector2f dir) {
        this.dir = dir;
    }

    @Override
    public Vector2f dir(int t, Vector2f dest) {
        return dest.set(dir);
    }
}
