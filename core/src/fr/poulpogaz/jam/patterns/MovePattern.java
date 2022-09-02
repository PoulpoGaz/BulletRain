package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public interface MovePattern {

    default void init(GameScreen game, Entity entity) {

    }

    void dir(int t, Vector2 last, GameScreen screen, Entity entity);


    MovePattern FOLLOW_MAP = new MovePattern() {
        @Override
        public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
            dest.set(0, -Constants.MAP_SCROLL_SPEED);
        }
    };

    MovePattern STATIC = new MovePattern() {
        @Override
        public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
            dest.set(0, 0);
        }
    };
}
