package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public class LinearPattern implements MovePattern {

    private final Vector2 dir;

    public LinearPattern(Vector2 dir) {
        this.dir = new Vector2(dir);
    }

    @Override
    public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
        dest.set(dir);
    }
}
