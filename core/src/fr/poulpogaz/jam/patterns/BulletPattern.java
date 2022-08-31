package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public interface BulletPattern {

    BulletPattern NO_BULLET = (game, entity, player) -> {};

    default void reset() {

    }

    void addBullets(GameScreen game, Entity entity, boolean player);
}
