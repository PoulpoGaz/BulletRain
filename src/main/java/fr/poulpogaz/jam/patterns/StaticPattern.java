package fr.poulpogaz.jam.patterns;

import org.joml.Vector2f;

public class StaticPattern implements MovePattern {
    @Override
    public Vector2f dir(float t) {
        return new Vector2f();
    }
}
