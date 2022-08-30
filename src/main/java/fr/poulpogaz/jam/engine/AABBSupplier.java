package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.entities.Entity;

public class AABBSupplier implements HitBoxSupplier {

    private final float width;
    private final float height;

    public AABBSupplier(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public HitBox getDetailedHitBox(Entity entity, HitBox last) {
        return HitBoxUtils.createAABB(last, entity.getX() - width / 2f, entity.getY() - height / 2f, width, height);
    }
}
