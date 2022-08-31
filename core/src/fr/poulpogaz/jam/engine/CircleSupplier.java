package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.entities.Entity;

public class CircleSupplier implements HitBoxSupplier {

    private final float radius;

    public CircleSupplier(float radius) {
        this.radius = radius;
    }

    @Override
    public HitBox getDetailedHitBox(Entity entity, HitBox p) {
        if (p instanceof Circle c) {
            c.setRadius(radius);
            c.getCenter().set(entity.getPos());
            return c;
        } else {
            return new Circle(entity.getPos(), radius);
        }
    }
}
