package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.ConvexPolygon;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.Mathf;
import org.joml.Math;
import org.joml.Vector2f;

public class BasicBullet extends TextureBullet {

    public BasicBullet(Game game,
                       boolean playerBullet,
                       MovePattern movePattern,
                       Vector2f pos,
                       ITexture texture,
                       float damage) {
        super(game, playerBullet, movePattern, pos, texture, damage);
    }

    @Override
    public Polygon getDetailedHitBox() {
        if (hitBox == null) {
            if (angle % Mathf.PI == 0) {
                hitBox = new AABB(
                        pos.x - texture.getWidth() / 2f, pos.y - texture.getHeight() / 2f,
                        texture.getWidth(), texture.getHeight());
            } else if (angle % Mathf.PI == Mathf.PI_2) {
                // inverted
                hitBox = new AABB(
                        pos.y - texture.getHeight() / 2f, pos.x - texture.getWidth() / 2f,
                        texture.getHeight(), texture.getWidth());

            } else {
                int cx = texture.getWidth() / 2;
                int cy = texture.getHeight() / 2;

                float cos = Math.cos(angle);
                float sin = Math.sin(angle);

                ConvexPolygon polygon = new ConvexPolygon();

                polygon.setCenter(new Vector2f(pos.x, pos.y));

                polygon.addPoint(new Vector2f(cos * -cx - sin * -cy, cos * -cy + sin * -cx));
                polygon.addPoint(new Vector2f(cos * +cx - sin * -cy, cos * -cy + sin * +cx));
                polygon.addPoint(new Vector2f(cos * +cx - sin * +cy, cos * +cy + sin * +cx));
                polygon.addPoint(new Vector2f(cos * -cx - sin * +cy, cos * +cy + sin * -cx));

                this.hitBox = polygon;
            }
        }

        return hitBox;
    }

    @Override
    public AABB aabb() {
        if (aabb == null) {
            aabb = getDetailedHitBox().getAABB();
        }

        return aabb;
    }
}
