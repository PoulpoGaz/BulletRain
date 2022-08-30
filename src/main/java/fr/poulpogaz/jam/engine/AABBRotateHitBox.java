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
                float cx = width / 2;
                float cy = height / 2;

                float cos = Math.cos(bullet.getAngle());
                float sin = Math.sin(bullet.getAngle());

                float x1 = cos * -cx - sin * -cy,  y1 =  cos * -cy + sin * -cx;
                float x2 = cos * +cx - sin * -cy,  y2 =  cos * -cy + sin * +cx;
                float x3 = cos * +cx - sin * +cy,  y3 =  cos * +cy + sin * +cx;
                float x4 = cos * -cx - sin * +cy,  y4 =  cos * +cy + sin * -cx;


                ConvexPolygon poly;
                if (last instanceof ConvexPolygon p) {
                    poly = p;
                    p.getModel(0).set(x1, y1);
                    p.getModel(1).set(x2, y2);
                    p.getModel(2).set(x3, y3);
                    p.getModel(3).set(x4, y4);
                    p.reloadPoints();

                } else {
                    poly = new ConvexPolygon();
                    poly.addPoint(new Vector2f(x1, y1));
                    poly.addPoint(new Vector2f(x2, y2));
                    poly.addPoint(new Vector2f(x3, y3));
                    poly.addPoint(new Vector2f(x4, y4));
                }

                bullet.getPos(poly.center());

                return poly;
            }
        }

        throw new IllegalStateException("not a bullet: " + entity);
    }
}
