package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Bullet;

public interface HitBoxSupplier {

    Polygon getDetailedHitBox(Bullet bullet);
}
