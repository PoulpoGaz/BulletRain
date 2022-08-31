package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;

public class AABBRotateSupplier implements HitBoxSupplier {

    private final float width;
    private final float height;

    public AABBRotateSupplier(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public HitBox getDetailedHitBox(Entity entity, HitBox last) {
        if (entity instanceof Bullet) {
            Bullet bullet = (Bullet) entity;

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

                float cos = MathUtils.cos(bullet.getAngle());
                float sin = MathUtils.sin(bullet.getAngle());

                float x1 = cos * -cx - sin * -cy,  y1 =  cos * -cy + sin * -cx;
                float x2 = cos * +cx - sin * -cy,  y2 =  cos * -cy + sin * +cx;
                float x3 = cos * +cx - sin * +cy,  y3 =  cos * +cy + sin * +cx;
                float x4 = cos * -cx - sin * +cy,  y4 =  cos * +cy + sin * -cx;


                Polygon poly;
                if (last instanceof Polygon) {
                    poly = (Polygon) last;
                    poly.getCenter().set(bullet.getPos());
                    poly.getModel(0).set(x1, y1);
                    poly.getModel(1).set(x2, y2);
                    poly.getModel(2).set(x3, y3);
                    poly.getModel(3).set(x4, y4);
                    poly.reloadPoints();

                } else {
                    poly = new Polygon();
                    poly.getCenter().set(bullet.getPos());
                    poly.addPoint(new Vector2(x1, y1));
                    poly.addPoint(new Vector2(x2, y2));
                    poly.addPoint(new Vector2(x3, y3));
                    poly.addPoint(new Vector2(x4, y4));
                }

                return poly;
            }
        }

        throw new IllegalStateException("not a bullet: " + entity);
    }
}
