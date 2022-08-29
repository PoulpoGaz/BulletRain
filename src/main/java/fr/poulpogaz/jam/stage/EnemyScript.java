package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.LinearPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.utils.BuilderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record EnemyScript(EnemyDescriptor enemy,
                          int triggerTime,
                          StartPos startPos, // enemy is centered on the x-axis
                          List<EnemyAction<MovePattern>> moves,
                          List<EnemyAction<BulletPattern>> bullets) {

    public Vector2f startPosVec() {
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

    public record StartPos(float xy, Location location) {

        public Vector2f asVec(EnemyDescriptor desc) {
            return switch (location) {
                case TOP -> new Vector2f(xy + Constants.HALF_WIDTH, -desc.height());
                case LEFT -> new Vector2f(-Constants.HALF_WIDTH - desc.width(), xy);
                case RIGHT -> new Vector2f(Constants.HALF_WIDTH + desc.width(), xy);
            };
        }
    }

    public enum Location {
        TOP,
        LEFT,
        RIGHT
    }

    public static class Builder {

        private static final Logger LOGGER = LogManager.getLogger(EnemyScript.Builder.class);
        
        private final StageBuilder parent;
        private final EnemyDescriptor enemy;

        private int triggerTime;
        private StartPos startPos;
        private final List<EnemyAction<MovePattern>> moves = new ArrayList<>();
        private final List<EnemyAction<BulletPattern>> bullets = new ArrayList<>();
        private Vector2f pos;
        
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

        public Builder moveTo(Vector2f dest, int duration) {
            if (startPos == null) {
                throw new BuilderException("Start pos not set");
            }

            if (pos == null) {
                pos = startPos.asVec(enemy);
            }

            float step = pos.distance(dest) / duration;

            Vector2f dir = dest.sub(pos, new Vector2f()).normalize(step);
            LinearPattern pattern = new LinearPattern(dir);

            addMove(new EnemyAction<>(duration, pattern), dest);
            return this;
        }

        public Builder wait(int duration) {
            moves.add(new EnemyAction<>(duration, MovePattern.FOLLOW_MAP));
            return this;
        }

        public Builder addMove(EnemyAction<MovePattern> move, Vector2f endPos) {
            moves.add(move);

            if (endPos != null) {
                pos = endPos;
            }

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
                LOGGER.warn("Setting starting pos after the first movement is discouraged");
            }

            return this;
        }
    }
}
