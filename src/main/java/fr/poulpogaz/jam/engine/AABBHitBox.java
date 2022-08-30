package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Entity;

public class AABBHitBox implements HitBoxSupplier {

    private final float width;
    private final float height;

    public AABBHitBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Polygon getDetailedHitBox(Entity entity, Polygon last) {
        return HitBoxUtils.createAABB(last, entity.getX() - width / 2f, entity.getY() - height / 2f, width, height);
    }
}
