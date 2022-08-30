package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import java.util.Objects;

public abstract class Entity {

    protected final Game game;
    protected final EntityRenderer renderer;
    protected Vector2f pos;

    protected Polygon detailedHitBox;
    protected AABB aabbHitBox;
    protected boolean hitBoxDirty = true;
    protected boolean aabbDirty = true;

    public Entity(Game game) {
        this.game = Objects.requireNonNull(game);
        this.renderer = Objects.requireNonNull(createRenderer());
    }

    public Entity(Game game, EntityRenderer renderer) {
        this.game = Objects.requireNonNull(game);
        this.renderer = Objects.requireNonNull(renderer);
    }

    protected EntityRenderer createRenderer() {
        return null;
    }

    public abstract void update(Input in, float delta);

    public void render(Graphics2D g2d, FontRenderer f2d) {
        renderer.render(g2d, f2d, game, this);

        if (Constants.SHOW_HITBOX) {
            AABB aabb = getAABB();

            g2d.setColor(Colors.RED);
            g2d.drawRect(aabb.getX(), aabb.getY(), aabb.getWidth(), aabb.getHeight());
        }
    }


    protected void markDirty() {
        hitBoxDirty = true;
        aabbDirty = true;
    }

    protected abstract Polygon getDetailedHitBoxImpl();

    protected AABB getAABBImpl() {
        return getDetailedHitBoxImpl().getAABB();
    }

    public Polygon getDetailedHitBox() {
        if (hitBoxDirty) {
            detailedHitBox = getDetailedHitBoxImpl();
        }

        return detailedHitBox;
    }

    public AABB getAABB() {
        if (aabbDirty) {
            aabbHitBox = getAABBImpl();
        }

        return aabbHitBox;
    }



    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public Vector2f getPos() {
        return getPos(new Vector2f());
    }

    public Vector2f getPos(Vector2f dest) {
        return dest.set(pos);
    }

    public EntityRenderer getRenderer() {
        return renderer;
    }
}
