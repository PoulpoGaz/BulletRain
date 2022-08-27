package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.ConvexPolygon;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.Mathf;
import org.joml.Math;
import org.joml.Vector2f;

public class BasicBullet extends TextureBullet {

    private AABB aabb;
    private Polygon polygon;

    public BasicBullet(Game game,
                       boolean playerBullet,
                       MovePattern movePattern,
                       Vector2f pos,
                       ITexture texture) {
        super(game, playerBullet, movePattern, pos, texture);
    }

    @Override
    public void update(Input in, float delta) {
        super.update(in, delta);
        aabb = null;
        polygon = null;
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        super.render(g2d, f2d);

        AABB aabb = aabb();
        g2d.setColor(Colors.RED);
        g2d.drawRect(aabb.getX(), aabb.getY(), aabb.getWidth(), aabb.getHeight());
    }

    @Override
    public Polygon getDetailedHitBox() {
        if (polygon == null) {
            if (angle % Mathf.PI == 0) {
                polygon = new AABB(
                        pos.x - texture.getWidth() / 2f, pos.y - texture.getHeight() / 2f,
                        texture.getWidth(), texture.getHeight());
            } else if (angle % Mathf.PI == Mathf.PI_2) {
                // inverted
                polygon = new AABB(
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

                this.polygon = polygon;
            }
        }

        return polygon;
    }

    @Override
    public AABB aabb() {
        if (aabb == null) {
            aabb = getDetailedHitBox().getAABB();
        }

        return aabb;
    }
}
