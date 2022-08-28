package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entity.Enemy;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.LinearPattern;
import fr.poulpogaz.jam.patterns.StaticPattern;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.BuilderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record EnemyScript(EnemyDescriptor enemy,
                          int triggerTime, // if negative, trigger when visible
                          Vector2f startPos,
                          List<EnemyAction> actions) {

    public Enemy createEnemy(Game game) {
        return new Enemy(game, enemy.life(), enemy.renderer(), new StaticPattern(), BulletPattern.NO_BULLET, startPos); // temp
    }

    public static class Builder {

        private static final Logger LOGGER = LogManager.getLogger(EnemyScript.Builder.class);
        
        private final StageBuilder parent;
        private final EnemyDescriptor enemy;

        private int triggerTime;
        private Vector2f startPos;
        private final List<EnemyAction> actions = new ArrayList<>();
        private Vector2f pos;
        
        public Builder(StageBuilder parent, EnemyDescriptor enemy) {
            this.parent = Objects.requireNonNull(parent);
            this.enemy = Objects.requireNonNull(enemy);
        }

        public StageBuilder build() {
            Objects.requireNonNull(startPos, "No start pos set");

            parent.addEnemyScript(new EnemyScript(enemy, triggerTime, startPos, actions));
            return parent;
        }

        public Builder moveTo(Vector2f dest, int duration) {
            if (startPos == null) {
                throw new BuilderException("Start pos not set");
            }

            if (pos == null) {
                pos = startPos;
            }

            float step = pos.distance(dest) / duration;

            Vector2f dir = dest.sub(pos, new Vector2f()).normalize(step);
            LinearPattern pattern = new LinearPattern(dir);

            addAction(new EnemyAction(duration, pattern, null), dest);
            return this;
        }

        public Builder wait(int duration) {
            actions.add(new EnemyAction(duration, new StaticPattern(), null));
            return this;
        }

        public Builder addAction(EnemyAction action, Vector2f endPos) {
            actions.add(action);

            if (endPos != null) {
                pos = endPos;
            }

            return this;
        }

        public int getTriggerTime() {
            return triggerTime;
        }

        public Builder setTriggerTime(int triggerTime) {
            this.triggerTime = triggerTime;
            return this;
        }

        public Vector2f getStartPos() {
            return startPos;
        }

        public Builder setStartPos(Vector2f startPos) {
            this.startPos = startPos;

            if (pos != null) {
                pos = startPos;
                LOGGER.warn("Setting starting pos after the first movement is discouraged");
            }

            return this;
        }
    }
}
