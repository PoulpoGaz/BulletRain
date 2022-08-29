package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;

public interface HitBoxSupplier {

    Polygon getDetailedHitBox(Entity entity);
}
