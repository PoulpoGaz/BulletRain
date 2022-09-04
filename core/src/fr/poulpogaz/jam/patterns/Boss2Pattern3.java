package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

public class Boss2Pattern3 extends AbstractMovePattern implements BulletPattern {

    private final String bullet;

    private final Vector2 dir = new Vector2();
    private int t;
    private float offset;

    public Boss2Pattern3(String bullet) {
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
        if (t > 60) {
            float thetaAdd = 10;
            float theta = offset;

            while (theta <= 360f + offset) {
                Vector2 vec = new Vector2().set(1, 0).rotateDeg(theta).scl(10f);

                Stage s = game.getStage();
                game.addBullet(s.createBullet(bullet, game, false, new LinearPattern(vec), entity.getPos().cpy()));

                theta += thetaAdd;
            }
        }

        if (t % (60 * 3) == 0) {
            offset += 5;
        }
    }

    public float getOffset() {
        return offset;
    }

    public int getT() {
        return t;
    }
}
