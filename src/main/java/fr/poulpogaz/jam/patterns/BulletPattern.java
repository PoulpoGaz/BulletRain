package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entity.Entity;
import fr.poulpogaz.jam.states.Game;

public interface BulletPattern {

    default void reset() {

    }

    void addBullets(Game game, Entity entity, boolean player);
}
