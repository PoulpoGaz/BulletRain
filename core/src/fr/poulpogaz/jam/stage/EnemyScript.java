package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.AbstractEnemy;
import fr.poulpogaz.jam.entities.Enemy;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.patterns.TargetMove;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.poulpogaz.jam.Constants.*;

public final class EnemyScript {

    private final EnemyDescriptor enemy;
    private final int triggerTime;
    private final StartPos startPos;
    private final List<EnemyAction<MovePattern>> moves;
    private final List<EnemyAction<BulletPattern>> bullets;

    EnemyScript(EnemyDescriptor enemy,
                int triggerTime,
                StartPos startPos, // enemy is centered on the x-axis
                List<EnemyAction<MovePattern>> moves,
                List<EnemyAction<BulletPattern>> bullets) {
        this.enemy = enemy;
        this.triggerTime = triggerTime;
        this.startPos = startPos;
        this.moves = moves;
        this.bullets = bullets;
    }

    public AbstractEnemy createEnemy(GameScreen screen) {
        return enemy.createEnemy(screen, this);
    }

    public EnemyDescriptor enemy() {
        return enemy;
    }

    public int triggerTime() {
        return triggerTime;
    }

    public StartPos startPos() {
        return startPos;
    }

    public List<EnemyAction<MovePattern>> moves() {
        return moves;
    }

    public List<EnemyAction<BulletPattern>> bullets() {
        return bullets;
    }

    public Vector2 startPosVec() {
        return startPos.asVec(enemy);
    }

    public BulletPattern getFirstBulletPattern() {
        if (bullets.isEmpty()) {
            return null;
        } else {
            return bullets.get(0).pattern();
        }
    }

    public MovePattern getFirstMovePattern() {
        if (moves.isEmpty()) {
            return null;
        } else {
            return moves.get(0).pattern();
        }
    }


    public EnemyAction<BulletPattern> getBulletPattern(int index) {
        return bullets.get(index);
    }

    public EnemyAction<MovePattern> getMovePattern(int index) {
        return moves.get(index);
    }

    public static final class StartPos {
        private final float xy;
        private final Location location;

        StartPos(float xy, Location location) {
            this.xy = xy;
            this.location = location;
        }

        public float xy() {
            return xy;
        }

        public Location location() {
            return location;
        }

        public Vector2 asVec(EnemyDescriptor desc) {
            switch (location) {
                case TOP: return new Vector2(xy + M_HALF_WIDTH, MAP_HEIGHT + desc.height());
                case LEFT: return new Vector2(-desc.width(), MAP_HEIGHT - xy);
                case RIGHT: return new Vector2(MAP_WIDTH + desc.width(), MAP_HEIGHT - xy);
            }

            throw new IllegalStateException();
        }
    }

    public enum Location {
        TOP,
        LEFT,
        RIGHT
    }

    public static class Builder {
        
        private final StageBuilder parent;
        private final EnemyDescriptor enemy;

        private int triggerTime;
        private StartPos startPos;
        private final List<EnemyAction<MovePattern>> moves = new ArrayList<>();
        private final List<EnemyAction<BulletPattern>> bullets = new ArrayList<>();

        private Vector2 pos;
        private int lastStart = -1;
        private int lastActionDuration = -1;
        private int currentT = 0;

        public Builder(StageBuilder parent, EnemyDescriptor enemy) {
            this.parent = Objects.requireNonNull(parent);
            this.enemy = Objects.requireNonNull(enemy);
        }

        public StageBuilder build() {
            Objects.requireNonNull(startPos, "No start pos set");

            if (triggerTime < 0) {
                throw new BuilderException("Negative trigger");
            }

            parent.addEnemyScript(new EnemyScript(enemy, triggerTime, startPos, moves, bullets));
            return parent;
        }

        public Builder moveTo(Vector2 dest, float speed) {
            if (startPos == null) {
                throw new BuilderException("Start pos not set");
            }

            dest.x += M_HALF_WIDTH;

            if (moves.isEmpty()) {
                pos = startPos.asVec(enemy);
            }

            Gdx.app.debug("DEBUG", "moveTo " + dest + " from " + pos + ", start = " + currentT + ", speed = " + speed);

            MovePattern move = new TargetMove.Pos(dest, speed);
            addMove(new EnemyAction<>(currentT, move), dest, (int) (dest.dst(pos) / speed));
            return this;
        }

        public Builder moveTo(Vector2 dest, int duration) {
            if (startPos == null) {
                throw new BuilderException("Start pos not set");
            }

            dest.x += M_HALF_WIDTH;

            if (moves.isEmpty()) {
                pos = startPos.asVec(enemy);
            }

            Gdx.app.debug("DEBUG", "moveTo " + dest + " from " + pos + ", start = " + currentT + ", duration = " + duration);

            TargetMove.PosDur posDur = new TargetMove.PosDur(dest, duration);
            addMove(new EnemyAction<>(currentT, posDur), dest, duration);
            return this;
        }

        public Builder follow(int duration) {
            Gdx.app.debug("DEBUG", "wait " + duration + " ticks, start: " + currentT);

            addMove(new EnemyAction<>(currentT, MovePattern.FOLLOW_MAP), pos, duration);
            return this;
        }

        public Builder slowFollow(int duration) {
            Gdx.app.debug("DEBUG", "wait " + duration + " ticks, start: " + currentT);

            addMove(new EnemyAction<>(currentT, MovePattern.SLOW_FOLLOW_MAP), pos, duration);
            return this;
        }


        public Builder wait(int duration) {
            Gdx.app.debug("DEBUG", "wait " + duration + " ticks, start: " + currentT);

            addMove(new EnemyAction<>(currentT, MovePattern.STATIC), pos, duration);
            return this;
        }

        public Builder addLastMove(MovePattern pattern) {
            Gdx.app.debug("DEBUG", "End move, start at " + currentT);

            return addMove(new EnemyAction<>(currentT, pattern), null, Integer.MAX_VALUE);
        }

        public Builder addMove(EnemyAction<MovePattern> move, Vector2 endPos, int duration) {
            if (endPos == null && moves.isEmpty()) {
                pos = startPos.asVec(enemy);
            } else {
                pos = endPos;
            }

            moves.add(move);

            lastStart = move.start();
            lastActionDuration = duration;

            currentT = lastStart + lastActionDuration;

            return this;
        }

        public Builder addBulletPattern(int start, BulletPattern pattern) {
            bullets.add(new EnemyAction<>(start, pattern));
            return this;
        }

        public int getTriggerTime() {
            return triggerTime;
        }

        public Builder setTriggerTime(int triggerTime) {
            this.triggerTime = triggerTime;
            return this;
        }

        public Builder setTriggerTimeS(float second) {
            this.triggerTime = (int) (second * 60);
            return this;
        }

        public StartPos getStartPos() {
            return startPos;
        }

        public Builder setStartPos(float xy, Location location) {
            return setStartPos(new StartPos(xy, location));
        }

        public Builder setStartPos(StartPos startPos) {
            this.startPos = startPos;

            if (!moves.isEmpty()) {
                Gdx.app.error("WARN", "Setting starting pos after the first movement is discouraged");
            }

            return this;
        }

        public int getLastActionDuration() {
            return lastActionDuration;
        }

        public void setLastActionDuration(int lastActionDuration) {
            this.lastActionDuration = lastActionDuration;
        }
    }
}
