package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;

public class LinearPattern implements MovePattern {

    private final Vector2 dir;

    public LinearPattern(Vector2 dir) {
        this.dir = new Vector2(dir);
    }

    @Override
    public Vector2 dir(int t, Vector2 dest) {
        return dest.set(dir);
    }
}
