package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public interface MovePattern {

    default void init(GameScreen game, Entity entity, int tStart) {

    }

    void dir(int t, Vector2 last, GameScreen screen, Entity entity);


    MovePattern FOLLOW_MAP = (t, dest, screen, entity) -> dest.set(0, -Constants.MAP_SCROLL_SPEED);
    MovePattern ANTI_FOLLOW_MAP = (t, dest, screen, entity) -> dest.set(0, Constants.MAP_SCROLL_SPEED);

    MovePattern STATIC = (t, dest, screen, entity) -> dest.set(0, 0);
}
