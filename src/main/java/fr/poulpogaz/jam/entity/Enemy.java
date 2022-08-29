package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.stage.EnemyAction;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import java.util.Objects;

public class Enemy extends Entity {

    private final EnemyScript script;

    private int bulletPatternIndex = 0;
    private int movePatternIndex = 0;
    private BulletPattern bulletPattern;
    private MovePattern movePattern;

    private int life;

    // time, in tick, since birth
    private int t = 0;
    private Vector2f dir;

    public Enemy(Game game, EnemyScript script) {
        super(game);
        this.script = script;
        this.pos = script.startPosVec();

        life = script.enemy().life();
        bulletPattern = Objects.requireNonNullElse(script.getFirstBulletPattern(), BulletPattern.NO_BULLET);
        movePattern = Objects.requireNonNullElse(script.getFirstMovePattern(), MovePattern.FOLLOW_MAP);

        dir = movePattern.dir(t);
    }

    @Override
    public void update(Input in, float delta) {
        dir = movePattern.dir(t);
        pos.add(dir);
        bulletPattern.addBullets(game, this, false);

        t++;
        nextBulletPattern();
        nextMovePattern();
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        script.enemy().renderer().render(g2d, f2d, game, this);
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
    public Polygon getDetailedHitBox() {
        return null;
    }

    @Override
    public AABB aabb() {
        return null;
    }

    public int getLife() {
        return life;
    }

    public boolean isDied() {
        return life <= 0;
    }
}
