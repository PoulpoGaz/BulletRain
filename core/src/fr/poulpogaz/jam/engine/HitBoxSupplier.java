package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.entities.Entity;

public interface HitBoxSupplier {

    HitBox getDetailedHitBox(Entity entity, HitBox last);
}
