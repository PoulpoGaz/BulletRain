package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.ConvexPolygon;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Vector2f;

public class AABBRotateHitBox implements HitBoxSupplier {

    private static final Logger LOGGER = LogManager.getLogger(AABBRotateHitBox.class);

    private final float width;
    private final float height;

    public AABBRotateHitBox(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Polygon getDetailedHitBox(Entity entity, Polygon last) {
        if (entity instanceof Bullet bullet) {
            if (bullet.getAngle() % Mathf.PI == 0) {
                return HitBoxUtils.createAABB(last,
                        bullet.getX() - width / 2f, bullet.getY() - height / 2f,
                        width, height);
            } else if (bullet.getAngle() % Mathf.PI == Mathf.PI_2) {
                // inverted
                return HitBoxUtils.createAABB(last,
                        bullet.getX() - height / 2f, bullet.getY() - width / 2f,
                        height, width);

            } else {
                ConvexPolygon poly;

                if (last instanceof ConvexPolygon p) {
                    poly = p;
                    poly.reset();
                } else {
                    poly = new ConvexPolygon();
                }

                float cx = width / 2;
                float cy = height / 2;

                float cos = Math.cos(bullet.getAngle());
                float sin = Math.sin(bullet.getAngle());

                poly.setCenter(bullet.getPos());

                poly.addPoint(new Vector2f(cos * -cx - sin * -cy, cos * -cy + sin * -cx));
                poly.addPoint(new Vector2f(cos * +cx - sin * -cy, cos * -cy + sin * +cx));
                poly.addPoint(new Vector2f(cos * +cx - sin * +cy, cos * +cy + sin * +cx));
                poly.addPoint(new Vector2f(cos * -cx - sin * +cy, cos * +cy + sin * -cx));

                return poly;
            }
        }

        throw new IllegalStateException("not a bullet: " + entity);
    }
}
