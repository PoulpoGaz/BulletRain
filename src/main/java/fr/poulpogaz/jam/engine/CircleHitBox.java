package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.Circle;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Entity;

public class CircleHitBox implements HitBoxSupplier {

    private float radius;

    public CircleHitBox(float radius) {
        this.radius = radius;
    }

    @Override
    public Polygon getDetailedHitBox(Entity entity) {
        return new Circle(radius, entity.getPos());
    }
}