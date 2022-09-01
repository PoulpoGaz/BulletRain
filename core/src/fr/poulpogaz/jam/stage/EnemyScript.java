package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.LinearPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.poulpogaz.jam.Constants.MAP_HEIGHT;
import static fr.poulpogaz.jam.Constants.M_HALF_WIDTH;

public final class EnemyScript {
    private final EnemyDescriptor enemy;
    private final float triggerTime;
    private final StartPos startPos;
    private final List<EnemyAction<MovePattern>> moves;
    private final List<EnemyAction<BulletPattern>> bullets;

    EnemyScript(EnemyDescriptor enemy,
                float triggerTime,
                StartPos startPos, // enemy is centered on the x-axis
                List<EnemyAction<MovePattern>> moves,
                List<EnemyAction<BulletPattern>> bullets) {
        this.enemy = enemy;
        this.triggerTime = triggerTime;
        this.startPos = startPos;
        this.moves = moves;
        this.bullets = bullets;
    }

    public EnemyDescriptor enemy() {
        return enemy;
    }

    public float triggerTime() {
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
                case LEFT: return new Vector2(-M_HALF_WIDTH - desc.width(), xy);
                case RIGHT: return new Vector2(M_HALF_WIDTH + desc.width(), xy);
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

        private float triggerTime;
        private StartPos startPos;
        private final List<EnemyAction<MovePattern>> moves = new ArrayList<>();
        private final List<EnemyAction<BulletPattern>> bullets = new ArrayList<>();
        private Vector2 pos;
        
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

        public Builder moveTo(Vector2 dest, float duration) {
            if (startPos == null) {
                throw new BuilderException("Start pos not set");
            }

            if (pos == null) {
                pos = startPos.asVec(enemy);
            }

            float step = pos.dst(dest) / duration;

            Vector2 dir = dest.cpy().sub(pos).nor().scl(step);
            LinearPattern pattern = new LinearPattern(dir);

            addMove(new EnemyAction<>(duration, pattern), dest);
            return this;
        }

        public Builder wait(float duration) {
            moves.add(new EnemyAction<>(duration, MovePattern.FOLLOW_MAP));
            return this;
        }

        public Builder addMove(EnemyAction<MovePattern> move, Vector2 endPos) {
            moves.add(move);

            if (endPos != null) {
                pos = endPos;
            }

            return this;
        }

        public Builder addBulletPattern(float start, BulletPattern pattern) {
            bullets.add(new EnemyAction<>(start, pattern));
            return this;
        }

        public float getTriggerTime() {
            return triggerTime;
        }

        public Builder setTriggerTime(float triggerTime) {
            this.triggerTime = triggerTime;
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

            if (pos != null) {
                pos = startPos.asVec(enemy);
                Gdx.app.error("WARN", "Setting starting pos after the first movement is discouraged");
            }

            return this;
        }
    }
}
