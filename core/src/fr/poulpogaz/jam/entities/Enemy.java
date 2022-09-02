package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.EnemyAction;
import fr.poulpogaz.jam.stage.EnemyDescriptor;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.utils.Mathf;
import fr.poulpogaz.jam.utils.Utils;

import java.util.Objects;

public class Enemy extends LivingEntity implements IRotateEntity {

    private final EnemyScript script;

    private int bulletPatternIndex = -1;
    private int movePatternIndex = -1;
    private BulletPattern bulletPattern;
    private MovePattern movePattern;

    // time, in tick, since birth
    private int t = 0;
    private final Vector2 dir;

    private float angle;
    private float angleDeg;

    public Enemy(GameScreen game, EnemyScript script) {
        super(game, script.enemy().renderer());
        this.script = script;
        this.pos = script.startPosVec();

        life = script.enemy().life();
        maxLife = life;

        nextBulletPattern();
        nextMovePattern();

        if (bulletPattern == null) {
            bulletPattern = BulletPattern.NO_BULLET;
        }
        if (movePattern == null) {
            movePattern = MovePattern.FOLLOW_MAP;
        }

        movePattern.init(game, this);

        this.dir = new Vector2();
        movePattern.dir(0, dir, game, this);

        computeAngle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isAlive()) {
            movePattern.dir(t, dir, game, this);
            pos.add(dir);

            bulletPattern.addBullets(game, this, false);

            t++;
            nextBulletPattern();
            nextMovePattern();
            computeAngle();

            if (!dir.epsilonEquals(0, 0)) {
                markDirty();
            }
        }
    }

    private void nextBulletPattern() {
        if (bulletPatternIndex + 1 < script.bullets().size()) {
            EnemyAction<BulletPattern> next = script.getBulletPattern(bulletPatternIndex + 1);

            if (next.start() <= t) {
                bulletPattern = next.pattern();
                bulletPatternIndex++;
            }
        }
    }

    private void nextMovePattern() {
        if (movePatternIndex + 1 < script.moves().size()) {
            EnemyAction<MovePattern> next = script.getMovePattern(movePatternIndex + 1);

            if (next.start() <= t) {
                movePattern = next.pattern();
                movePattern.init(game, this);
                movePatternIndex++;
            }
        }
    }

    public void kill() {
        life = 0;
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return script.enemy().hitBox().getDetailedHitBox(this, detailedHitBox);
    }

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

    public EnemyDescriptor getDescriptor() {
        return script.enemy();
    }
}
