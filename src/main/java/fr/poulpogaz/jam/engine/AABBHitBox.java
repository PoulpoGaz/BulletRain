package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Bullet;

public class AABBHitBox implements HitBoxSupplier {

    private final float width;
    private final float height;

    public AABBHitBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Polygon getDetailedHitBox(Bullet bullet) {
        return new AABB(bullet.getX(), bullet.getY(), width, height);
    }
}
