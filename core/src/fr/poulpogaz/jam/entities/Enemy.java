package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.EnemyAction;
import fr.poulpogaz.jam.stage.EnemyDescriptor;
import fr.poulpogaz.jam.stage.EnemyScript;

public class Enemy extends AbstractEnemy {

    private final EnemyScript script;

    private int bulletPatternIndex = -1;
    private int movePatternIndex = -1;

    public Enemy(GameScreen game, EnemyScript script) {
        super(game, script.enemy().renderer());
        this.script = script;
        this.pos = script.startPosVec();

        life = script.enemy().life();
        maxLife = life;

        init();
    }

    @Override
    protected void nextPatterns() {
        if (bulletPatternIndex + 1 < script.bullets().size()) {
            EnemyAction<BulletPattern> next = script.getBulletPattern(bulletPatternIndex + 1);

            if (next.start() <= t) {
                bulletPattern = next.pattern();
                bulletPatternIndex++;
            }
        }

        if (movePatternIndex + 1 < script.moves().size()) {
            EnemyAction<MovePattern> next = script.getMovePattern(movePatternIndex + 1);

            if (next.start() <= t) {
                movePattern = next.pattern();
                movePattern.init(game, this, t);
                movePatternIndex++;
            }
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        return script.enemy().hitBox().getDetailedHitBox(this, detailedHitBox);
    }

    public EnemyDescriptor getDescriptor() {
        return script.enemy();
    }
}
