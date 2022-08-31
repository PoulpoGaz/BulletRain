package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.EnemyAction;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.utils.Utils;

import java.util.Objects;

public class Enemy extends LivingEntity {

    private final EnemyScript script;

    private int bulletPatternIndex = 0;
    private int movePatternIndex = 0;
    private BulletPattern bulletPattern;
    private MovePattern movePattern;

    // time, in tick, since birth
    private int t = 0;
    private Vector2 dir;

    public Enemy(GameScreen game, EnemyScript script) {
        super(game, script.enemy().renderer());
        this.script = script;
        this.pos = script.startPosVec();

        life = script.enemy().life();
        bulletPattern = Utils.requireNonNullElse(script.getFirstBulletPattern(), BulletPattern.NO_BULLET);
        movePattern = Utils.requireNonNullElse(script.getFirstMovePattern(), MovePattern.FOLLOW_MAP);

        dir = movePattern.dir(0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isAlive()) {
            movePattern.dir(t, dir);
            pos.add(dir);
            bulletPattern.addBullets(game, this, false);

            t++;
            nextBulletPattern();
            nextMovePattern();

            if (!dir.epsilonEquals(0, 0)) {
                markDirty();
            }
        }
    }

    private void nextBulletPattern() {
        if (bulletPatternIndex + 1 < script.bullets().size()) {
            EnemyAction<BulletPattern> next = script.getBulletPattern(bulletPatternIndex + 1);

            if (next.start() >= t) {
                bulletPattern = next.pattern();
                bulletPatternIndex++;
            }
        }
    }

    private void nextMovePattern() {
        if (movePatternIndex + 1 < script.bullets().size()) {
            EnemyAction<MovePattern> next = script.getMovePattern(movePatternIndex + 1);

            if (next.start() >= t) {
                movePattern = next.pattern();
                movePatternIndex++;
            }
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return script.enemy().hitBox().getDetailedHitBox(this, detailedHitBox);
    }

    public int getLife() {
        return life;
    }
}
