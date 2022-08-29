package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.ConvexPolygon;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;
import org.joml.Math;
import org.joml.Vector2f;

public class AABBRotateHitBox implements HitBoxSupplier {
    
    private final float width;
    private final float height;

    public AABBRotateHitBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Polygon getDetailedHitBox(Entity entity) {
        if (entity instanceof Bullet bullet) {
            if (bullet.getAngle() % Mathf.PI == 0) {
                return new AABB(
                        bullet.getX() - width / 2f, bullet.getY() - height / 2f,
                        width, height);
            } else if (bullet.getAngle() % Mathf.PI == Mathf.PI_2) {
                // inverted
                return new AABB(
                        bullet.getX() - height / 2f, bullet.getY() - width / 2f,
                        height, width);

            } else {
                float cx = width / 2;
                float cy = height / 2;

                float cos = Math.cos(bullet.getAngle());
                float sin = Math.sin(bullet.getAngle());

                ConvexPolygon polygon = new ConvexPolygon();

                polygon.setCenter(bullet.getPos());

                polygon.addPoint(new Vector2f(cos * -cx - sin * -cy, cos * -cy + sin * -cx));
                polygon.addPoint(new Vector2f(cos * +cx - sin * -cy, cos * -cy + sin * +cx));
                polygon.addPoint(new Vector2f(cos * +cx - sin * +cy, cos * +cy + sin * +cx));
                polygon.addPoint(new Vector2f(cos * -cx - sin * +cy, cos * +cy + sin * -cx));

                return polygon;
            }
        }

        throw new IllegalStateException("not a bullet: " + entity);
    }
}
