package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Circle;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class CircleBullet extends TextureBullet {

    public CircleBullet(Game game, boolean playerBullet, MovePattern movePattern, Vector2f pos, ITexture texture, int damage) {
        super(game, playerBullet, movePattern, pos, texture, damage);
    }

    @Override
    public Polygon getDetailedHitBox() {
        if (hitBox != null) {
            hitBox = new Circle(texture.getWidth() / 2f, pos);
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
