package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;

public abstract class AbstractEnemy extends LivingEntity implements IRotateEntity {

    protected BulletPattern bulletPattern;
    protected MovePattern movePattern;

    // time, in tick, since birth
    protected int t = 0;
    protected final Vector2 dir = new Vector2();

    protected float angle;
    protected float angleDeg;

    public AbstractEnemy(GameScreen game, EntityRenderer renderer) {
        super(game, renderer);

    }

    protected void init() {
        nextPatterns();

        if (bulletPattern == null) {
            bulletPattern = BulletPattern.NO_BULLET;
        }
        if (movePattern == null) {
            movePattern = MovePattern.FOLLOW_MAP;
        }

        movePattern.init(game, this, 0);
        movePattern.dir(0, dir, game, this);

        computeAngle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isAlive()) {
            movePattern.dir(t, dir, game, this);
            pos.add(dir);
            computeAngle();
            onMove();

            bulletPattern.addBullets(game, this, false);

            t++;
            nextPatterns();

            if (!dir.epsilonEquals(0, 0)) {
                markDirty();
            }
        }
    }

    protected void onMove() {

    }

    protected abstract void nextPatterns();


    private void computeAngle() {
        if (dir.epsilonEquals(0, 0)) {
            angle = game.getPlayer().getPos().cpy().sub(pos).angleRad();
        } else {
            angle = (float) (Math.atan2(dir.y, dir.x));
        }

        angleDeg = (float) Math.toDegrees(angle);
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getAngleDeg() {
        return angleDeg;
    }
}
