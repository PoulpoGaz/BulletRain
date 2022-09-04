package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

public class Boss2Pattern2 extends AbstractMovePattern implements BulletPattern {

    private final String bullet;

    private final Vector2 dir = new Vector2();
    private int t;

    public Boss2Pattern2(String bullet) {
        this.bullet = bullet;
    }

    @Override
    protected void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
        int mod = t % (60 * 3);
        this.t = mod;

        if (mod == 0) {
            if (entity.getX() < 50) {
                dir.set(1, 0);
            } else if (entity.getX() > Constants.MAP_WIDTH - 50) {
                dir.set(-1, 0);
            } else {
                boolean left = Mathf.RANDOM.nextBoolean();

                if (left) {
                    dir.set(-1, 0);
                } else {
                    dir.set(1, 0);
                }
            }
        }

        if (mod < 60) {
            float speed = (float) (Math.exp(-mod / 60f) - Math.exp(-1)) * (Mathf.random(3f, 5f));

            last.set(dir).scl(speed);
        }
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (t > 60 * 2) {
            return;
        }

        float rad = Mathf.random(0, Mathf.PI * 2);
        Vector2 dir = new Vector2(1, 0).rotateRad(rad);

        Stage s = game.getStage();
        game.addBullet(s.createBullet(bullet, game, false, new BulletMovePattern(dir, game.getPlayer().getPos().cpy()), entity.getPos().cpy()));
    }

    private static class BulletMovePattern extends AbstractMovePattern {

        private final Vector2 dir;
        private final Vector2 oldPlayerPos;

        public BulletMovePattern(Vector2 dir, Vector2 oldPlayerPos) {
            this.dir = dir;
            this.oldPlayerPos = oldPlayerPos;
        }

        @Override
        public void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
            if (t < 60 * 2) {
                float speed = (float) (Math.exp(-t / (60f * 2)) - Math.exp(-1)) * 2f;

                last.set(dir).scl(speed);
                return;
            } else if (t == 60 * 2) {
                float xAdd = Mathf.random(-20, 20);
                float yAdd = Mathf.random(-20, 20);

                dir.set(oldPlayerPos).add(xAdd, yAdd).sub(entity.getPos()).nor();
            }

            last.set(dir).scl(5f);
        }
    }
}
