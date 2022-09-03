package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;

public abstract class TargetMove implements MovePattern {

    protected final Vector2 dir = new Vector2();

    @Override
    public void init(GameScreen game, Entity entity, int tStart) {
        dir.set(getDest(game, entity))
                .sub(entity.getPos())
                .scl(getSpeed(game, entity) / dir.len());

        Gdx.app.debug("DEBUG", "Init target move from " + entity.getPos() + " to " + getDest(game, entity) + ". Dir= " + dir);
    }

    protected abstract float getSpeed(GameScreen gameScreen, Entity e);
    protected abstract Vector2 getDest(GameScreen gameScreen, Entity e);

    @Override
    public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
        dest.set(dir);
    }

    public static class Player extends TargetMove {

        private final float speed;

        public Player(float speed) {
            this.speed = speed;
        }

        @Override
        protected float getSpeed(GameScreen gameScreen, Entity e) {
            return speed;
        }

        @Override
        protected Vector2 getDest(GameScreen gameScreen, Entity e) {
            return gameScreen.getPlayer().getPos();
        }
    }

    public static class Pos extends TargetMove {

        private final Vector2 dest;
        private final float speed;

        public Pos(Vector2 dest, float speed) {
            this.dest = dest;
            this.speed = speed;
        }

        @Override
        protected float getSpeed(GameScreen gameScreen, Entity e) {
            return speed;
        }

        @Override
        protected Vector2 getDest(GameScreen gameScreen, Entity e) {
            return dest;
        }
    }

    public static class PosDur extends TargetMove {

        private final Vector2 dest;
        private final int duration;

        public PosDur(Vector2 dest, int duration) {
            this.dest = dest;
            this.duration = duration;
        }

        @Override
        protected float getSpeed(GameScreen gameScreen, Entity e) {
            return e.getPos().dst(dest) / duration;
        }

        @Override
        protected Vector2 getDest(GameScreen gameScreen, Entity e) {
            return dest;
        }
    }
}
