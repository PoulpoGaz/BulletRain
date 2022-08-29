package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.stage.IBulletDescriptor;
import fr.poulpogaz.jam.states.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

public class Bullet extends Entity {

    private static final Logger LOGGER = LogManager.getLogger(Bullet.class);

    protected final IBulletDescriptor descriptor;
    protected final MovePattern movePattern;
    protected final boolean playerBullet;

    protected int t = 0;
    protected Vector2f dir;

    protected float angle;

    public Bullet(Game game,
                  IBulletDescriptor descriptor,
                  boolean playerBullet,
                  MovePattern movePattern,
                  Vector2f pos) {
        super(game, descriptor.renderer());
        this.pos = new Vector2f(pos);
        this.descriptor = descriptor;
        this.movePattern = movePattern;
        this.playerBullet = playerBullet;

        dir = movePattern.dir(0);
        angle =  computeAngle();
    }

    @Override
    public void update(Input in, float delta) {
        movePattern.dir(t, dir);
        pos.add(dir);
        t++;

        angle = computeAngle();
        clean();
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        super.render(g2d, f2d);

        if (Constants.SHOW_HITBOX) {
            AABB aabb = getAABB();

            g2d.setColor(Colors.RED);
            g2d.drawRect(aabb.getX(), aabb.getY(), aabb.getWidth(), aabb.getHeight());
        }
    }

    @Override
    protected Polygon getDetailedHitBoxImpl() {
        return descriptor.hitBoxSupplier().getDetailedHitBox(this);
    }

    private float computeAngle() {
        return (float) (Math.atan2(dir.y, dir.x));
    }


    public IBulletDescriptor getDescriptor() {
        return descriptor;
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2f getDirection() {
        return getDirection(new Vector2f());
    }

    public Vector2f getDirection(Vector2f dest) {
        return dest.set(dir);
    }

    public float getDamage() {
        return descriptor.damage();
    }
}