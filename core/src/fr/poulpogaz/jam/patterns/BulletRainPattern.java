package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

import static fr.poulpogaz.jam.Constants.*;

public class BulletRainPattern implements BulletPattern {

    private static final float MIN_X = M_Q_WIDTH;
    private static final float MIN_Y = M_Q_HEIGHT;

    private final String bullet;
    private final int reload;
    private final Vector2 dir;
    private final float minSpeed;
    private final float maxSpeed;

    private int tick;

    public BulletRainPattern(String bullet, int reload, Vector2 dir, float minSpeed, float maxSpeed) {
        this.bullet = bullet;
        this.reload = reload;
        this.dir = dir.nor();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (tick == 0) {
            float xLen = (MAP_WIDTH - MIN_X) + OUTER_SCREEN_SIZE;
            float yLen = (MAP_HEIGHT - MIN_Y) + OUTER_SCREEN_SIZE;

            float xy = Mathf.random(0, xLen + yLen);

            float speed = Mathf.random(minSpeed, maxSpeed);

            Vector2 pos;
            if (xy >= yLen) {
                // top
                float x = xy - yLen + MIN_X;
                pos = new Vector2(x, MAP_HEIGHT + OUTER_SCREEN_SIZE);

            } else {
                // left
                float y = xy + MIN_Y;
                pos = new Vector2(MAP_WIDTH + OUTER_SCREEN_SIZE, y);
            }

            Stage s = game.getStage();
            game.addBullet(s.createBullet(bullet, game, false, new LinearPattern(dir.cpy().scl(speed)), pos));
        }


        tick++;
        if (tick >= reload) {
            tick = 0;
        }
    }
}
