package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public abstract class AbstractMovePattern implements MovePattern {

    private int tStart;

    @Override
    public void init(GameScreen game, Entity entity, int tStart) {
        this.tStart = tStart;
    }

    @Override
    public void dir(int t, Vector2 last, GameScreen screen, Entity entity) {
        dirImpl(t - tStart, last, screen, entity);
    }

    protected abstract void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity);
}
