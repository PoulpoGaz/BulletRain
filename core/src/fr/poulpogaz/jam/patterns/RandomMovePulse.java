package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;

public class RandomMovePulse extends AbstractMovePattern {

    private static final float MIN_X = 20;
    private static final float MAX_X = Constants.MAP_WIDTH - MIN_X;
    private static final float MAX_Y = Constants.MAP_HEIGHT - 20;
    private static final float MIN_Y = Constants.M_HALF_HEIGHT - 20;

    private final int period;

    private final Vector2 dest = new Vector2();
    private final Vector2 dir = new Vector2();

    public RandomMovePulse(int period) {
        this.period = period;
    }

    @Override
    public void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
        int tMod = t % period;

        if (tMod == 0) {
            Vector2 pos = entity.getPos();

            float minX = pos.x - 200;
            float minY = pos.y - 200;
            float maxX = pos.x + 200;
            float maxY = pos.y + 200;

            if (minX < MIN_X) {
                maxX = maxX - minX;
                minX = MIN_X;
            } else if (maxX > MAX_X) {
                minX = minX - (maxX - MAX_X);
                maxX = MAX_X;
            }

            if (minY < MIN_Y) {
                maxY = maxY + (MIN_Y - minY);
                minY = MIN_Y;
            } else if (maxY > MAX_Y) {
                minY = minY - (maxY - MAX_Y);
                maxY = MAX_Y;
            }

            dest.set(Mathf.random(minX, maxX), Mathf.random(minY, maxY));
            dir.set(dest).sub(entity.getPos()).nor();
        }

        float speed = 5f * (float) (Math.exp((float) -tMod / period) - Math.exp(-1));

        if (speed < 0) {
            speed = 0;
        }

        last.set(dir).scl(speed);
    }
}
