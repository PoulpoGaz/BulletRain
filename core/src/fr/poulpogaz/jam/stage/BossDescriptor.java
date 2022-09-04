package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.TextureEntityRenderer;
import fr.poulpogaz.jam.entities.TextureRotate;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.patterns.TargetMove;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.poulpogaz.jam.Constants.M_HALF_WIDTH;

public class BossDescriptor {

    private final EntityRenderer renderer;
    private final HitBoxSupplier hitBox;
    private final int width;
    private final int height;
    private final List<BossPhase> phases;
    private final int life;

    public BossDescriptor(EntityRenderer renderer, HitBoxSupplier hitBox, int width, int height, List<BossPhase> phases, int life) {
        this.renderer = renderer;
        this.hitBox = hitBox;
        this.width = width;
        this.height = height;
        this.phases = phases;
        this.life = life;
    }

    public EntityRenderer renderer() {
        return renderer;
    }

    public HitBoxSupplier hitBox() {
        return hitBox;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<BossPhase> phases() {
        return phases;
    }

    public int life() {
        return life;
    }

    public static class Builder {

        private final StageBuilder parent;

        private EntityRenderer renderer;
        private HitBoxSupplier hitBox;
        private int life;
        private int width;
        private int height;

        // phases
        private List<BossPhase> phases = new ArrayList<>();

        private int onLife;
        private List<EnemyAction<MovePattern>> moves;
        private List<EnemyAction<BulletPattern>> bullets;

        private Vector2 pos;
        private int lastStart = -1;
        private int lastActionDuration = -1;
        private int currentT = 0;

        public Builder(StageBuilder parent) {
            this.parent = parent;
        }

        public StageBuilder build() {
            endPhase();

            Objects.requireNonNull(renderer);
            Objects.requireNonNull(hitBox);

            if (phases.isEmpty()) {
                throw new BuilderException("No phases");
            }

            if (life <= 0) {
                throw new BuilderException("enemy is dead");
            }
            if (width <= 0) {
                throw new BuilderException("Negative or null width");
            }
            if (height <= 0) {
                throw new BuilderException("Negative or null height");
            }

            return parent.setBoss(new BossDescriptor(renderer, hitBox, width, height, phases, life));
        }

        public EntityRenderer getRenderer() {
            return renderer;
        }

        public Builder setRenderer(EntityRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder setTexture(String texture) {
            this.renderer = new TextureEntityRenderer(texture);
            return this;
        }

        public Builder setTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureEntityRenderer(texture, x, y, w, h);
            this.width = w;
            this.height = h;
            return this;
        }

        public Builder setRotateTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureRotate(texture, x, y, w, h);
            this.width = w;
            this.height = h;
            return this;
        }

        public HitBoxSupplier getHitBox() {
            return hitBox;
        }

        public Builder setHitBox(HitBoxSupplier hitBox) {
            this.hitBox = hitBox;
            return this;
        }

        public Builder newPhase() {
            return newPhase(life);
        }

        public Builder newPhase(int onLife) {
            endPhase();
            this.onLife = onLife;
            moves = new ArrayList<>();
            bullets = new ArrayList<>();

            lastStart = -1;
            lastActionDuration = -1;
            currentT = 0;

            if (phases.isEmpty()) {
                pos = new Vector2(Constants.M_HALF_WIDTH, Constants.MAP_HEIGHT + height);
            }

            return this;
        }

        public Builder endPhase() {
            if (moves == null || bullets == null || (moves.isEmpty() && bullets.isEmpty())) {
                return this;
            }

            phases.add(new BossPhase(onLife, moves, bullets));

            moves = null;
            bullets = null;

            return this;
        }
        

        public Builder moveTo(Vector2 dest, float speed) {
            dest.x += M_HALF_WIDTH;

            Gdx.app.debug("DEBUG", "moveTo " + dest + " from " + pos + ", start = " + currentT + ", speed = " + speed);

            MovePattern move = new TargetMove.Pos(dest, speed);
            addMove(new EnemyAction<>(currentT, move), dest, (int) (dest.dst(pos) / speed));
            return this;
        }

        public Builder moveTo(Vector2 dest, int duration) {
            dest.x += M_HALF_WIDTH;

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

        public Builder wait(int duration) {
            Gdx.app.debug("DEBUG", "wait " + duration + " ticks, start: " + currentT);

            addMove(new EnemyAction<>(currentT, MovePattern.STATIC), pos, duration);
            return this;
        }

        public Builder addLastMove(MovePattern pattern) {
            Gdx.app.debug("DEBUG", "End move, start at " + currentT);

            return addMove(new EnemyAction<>(currentT, pattern), null, Integer.MAX_VALUE);
        }

        public Builder addMove(int start, MovePattern move, Vector2 endPos, int duration) {
            return addMove(new EnemyAction<>(start, move), endPos, duration);
        }

        public Builder addMove(EnemyAction<MovePattern> move, Vector2 endPos, int duration) {
            pos = endPos;

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



        public int getLastActionDuration() {
            return lastActionDuration;
        }

        public void setLastActionDuration(int lastActionDuration) {
            this.lastActionDuration = lastActionDuration;
        }






        public int getLife() {
            return life;
        }

        public Builder setLife(int life) {
            this.life = life;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }
    }
}
