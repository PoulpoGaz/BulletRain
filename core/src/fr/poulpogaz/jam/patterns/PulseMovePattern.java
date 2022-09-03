package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public class PulseMovePattern extends AbstractMovePattern {

    private final Vector2 dir;
    private final int period;

    public PulseMovePattern(Vector2 dir, int period) {
        this.dir = dir;
        this.period = period;
    }


    @Override
    public void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
        float x = 3f * (t % period) / period;
        float scl = (float) Math.exp(-x + 1);

        last.set(dir).scl(scl);
    }
}
