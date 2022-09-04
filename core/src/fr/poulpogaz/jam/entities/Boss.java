package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.BossDescriptor;
import fr.poulpogaz.jam.stage.BossPhase;
import fr.poulpogaz.jam.stage.EnemyAction;

import java.util.List;

public class Boss extends AbstractEnemy {

    private final BossDescriptor descriptor;

    private int currentPhase;
    private int tickPhaseStart;

    private int bulletPatternIndex = -1;
    private int movePatternIndex = -1;

    private boolean isAppearing;

    public Boss(GameScreen game, BossDescriptor descriptor) {
        super(game, descriptor.renderer());
        this.descriptor = descriptor;
        this.pos = new Vector2(Constants.M_HALF_WIDTH, Constants.MAP_HEIGHT + descriptor.height());

        life = descriptor.life();
        maxLife = life;
        currentPhase = 0;
        tickPhaseStart = 0;

        isAppearing = true;

        init();
    }

    @Override
    public void update(float delta) {
        if (isAppearing) {
            pos.add(0, -Constants.MAP_SCROLL_SPEED);

            if (t == 120) {
                isAppearing = false;
                tickPhaseStart = t;
                bulletPatternIndex = -1;
                movePatternIndex = -1;
                nextPatterns();
            }

            t++;
            markDirty();
        } else {
            super.update(delta);
        }
    }

    protected void nextPhase() {
        List<BossPhase> p = descriptor.phases();

        BossPhase next = currentPhase + 1 < p.size() ? p.get(currentPhase + 1) : null;

        if (next != null && next.onLife() >= life) {
            currentPhase++;

            bulletPattern = BulletPattern.NO_BULLET;
            movePattern = MovePattern.STATIC;
            bulletPatternIndex = -1;
            movePatternIndex = -1;
            tickPhaseStart = t;

            nextPatterns();
        }
    }

    @Override
    protected void nextPatterns() {
        List<BossPhase> p = descriptor.phases();

        BossPhase phase = p.get(currentPhase);

        int t = this.t - tickPhaseStart;
        if (bulletPatternIndex + 1 < phase.bullets().size()) {
            EnemyAction<BulletPattern> next = phase.getBulletPattern(bulletPatternIndex + 1);

            if (next.start() <= t) {
                bulletPattern = next.pattern();
                bulletPatternIndex++;
            }
        }

        if (movePatternIndex + 1 < phase.moves().size()) {
            EnemyAction<MovePattern> next = phase.getMovePattern(movePatternIndex + 1);

            if (next.start() <= t) {
                movePattern = next.pattern();
                movePattern.init(game, this, this.t);
                movePatternIndex++;
            }
        } else if (phase.moves().isEmpty()) {
            movePattern = MovePattern.STATIC;
        }
    }

    @Override
    public void hit(Entity b, float damage) {
        if (!isAppearing) {
            super.hit(b, damage);
            nextPhase();
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return descriptor.hitBox().getDetailedHitBox(this, detailedHitBox);
    }

    public boolean isAppearing() {
        return isAppearing;
    }

    public float bossAppearAnimationPercent() {
        if (t >= 120) {
            return 1;
        } else {
            return t / 120f;
        }
    }

    public BossDescriptor getDescriptor() {
        return descriptor;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }
}
