package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.IBulletDescriptor;

public class Bullet extends Entity implements IRotateEntity {

    protected final IBulletDescriptor descriptor;
    protected final MovePattern movePattern;
    protected final boolean playerBullet;

    protected int t = 0;
    protected Vector2 dir;

    protected float angle;
    protected float angleDeg;

    public Bullet(GameScreen game,
                  IBulletDescriptor descriptor,
                  boolean playerBullet,
                  MovePattern movePattern,
                  Vector2 pos) {
        super(game, descriptor.renderer());
        this.pos = pos;
        this.descriptor = descriptor;
        this.movePattern = movePattern;
        this.playerBullet = playerBullet;

        movePattern.init(game, this, 0);

        this.dir = new Vector2();
        movePattern.dir(0, dir, game, this);

        computeAngle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        movePattern.dir(t, dir, game, this);
        pos.add(dir);
        t++;

        computeAngle();

        if (!dir.epsilonEquals(0, 0)) {
            markDirty();
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return descriptor.hitBoxSupplier().getDetailedHitBox(this, detailedHitBox);
    }

    private void computeAngle() {
        if (dir.x != 0 || dir.y != 0) {
            angle = (float) (Math.atan2(dir.y, dir.x));
            angleDeg = (float) Math.toDegrees(angle);
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

    public float getAngleDeg() {
        return angleDeg;
    }

    public Vector2 getDirection() {
        return dir;
    }

    public float getDamage() {
        return descriptor.damage();
    }
}
