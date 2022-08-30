package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.MovePattern;
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
        this.pos = pos;
        this.descriptor = descriptor;
        this.movePattern = movePattern;
        this.playerBullet = playerBullet;

        dir = movePattern.dir(0);
        angle = computeAngle();
    }

    @Override
    public void update(Input in, float delta) {
        super.update(in, delta);

        movePattern.dir(t, dir);
        pos.add(dir);
        t++;

        angle = computeAngle();

        if (!dir.equals(0, 0)) {
            markDirty();
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return descriptor.hitBoxSupplier().getDetailedHitBox(this, detailedHitBox);
    }

    private float computeAngle() {
        if (dir.x == 0 && dir.y == 0) {
            return 0;
        } else {
            return (float) (Math.atan2(dir.y, dir.x));
        }
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
