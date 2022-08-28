package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entity.Entity;
import fr.poulpogaz.jam.states.Game;

public interface BulletPattern {

    class NoBulletPattern implements BulletPattern {

        @Override
        public void addBullets(Game game, Entity entity, boolean player) {

        }
    }

    BulletPattern NO_BULLET = new NoBulletPattern();

    default void reset() {

    }

    void addBullets(Game game, Entity entity, boolean player);
}
