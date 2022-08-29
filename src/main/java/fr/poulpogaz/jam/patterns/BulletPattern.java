package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.states.Game;

public interface BulletPattern {

    BulletPattern NO_BULLET = (game, entity, player) -> {};

    default void reset() {

    }

    void addBullets(Game game, Entity entity, boolean player);
}
