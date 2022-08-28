package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public abstract class AbstractBullet extends Entity {

    protected final boolean playerBullet;
    protected final MovePattern movePattern;
    protected final float damage;

    protected Polygon hitBox;
    protected AABB aabb;

    protected int t = 0;
    protected Vector2f dir;

    protected float angle;

    public AbstractBullet(Game game,
                          boolean playerBullet,
                          MovePattern movePattern,
                          Vector2f pos,
                          float damage) {
        super(game);
        this.pos = pos;
        this.playerBullet = playerBullet;
        this.movePattern = movePattern;
        this.damage = damage;

        dir = movePattern.dir(0);
        angle =  computeAngle();
    }

    @Override
    public void update(Input in, float delta) {
        dir = movePattern.dir(t);
        pos.add(dir);
        t++;

        angle = computeAngle();

        hitBox = null;
        aabb = null;
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        if (Constants.SHOW_HITBOX) {
            AABB aabb = aabb();
            g2d.setColor(Colors.RED);
            g2d.drawRect(aabb.getX(), aabb.getY(), aabb.getWidth(), aabb.getHeight());
        }
    }

    private float computeAngle() {
        return (float) (Math.atan2(dir.y, dir.x));
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }
}
