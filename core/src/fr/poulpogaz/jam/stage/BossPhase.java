package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;

import java.util.List;

public class BossPhase {

    private final int life;
    private final List<EnemyAction<MovePattern>> moves;
    private final List<EnemyAction<BulletPattern>> bullets;

    public BossPhase(int life, List<EnemyAction<MovePattern>> moves, List<EnemyAction<BulletPattern>> bullets) {
        this.life = life;
        this.moves = moves;
        this.bullets = bullets;
    }

    public EnemyAction<BulletPattern> getBulletPattern(int index) {
        return bullets.get(index);
    }

    public EnemyAction<MovePattern> getMovePattern(int index) {
        return moves.get(index);
    }

    public int onLife() {
        return life;
    }

    public List<EnemyAction<MovePattern>> moves() {
        return moves;
    }

    public List<EnemyAction<BulletPattern>> bullets() {
        return bullets;
    }
}
