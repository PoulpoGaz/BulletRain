package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;

public class SpiralPattern implements BulletPattern {

    private final String bullet;
    private final float reload;
    private final float angularSpeed;
    private final float speed;

    private float degrees;
    private int tick;

    public SpiralPattern(String bullet, float reload, float angularSpeed, float speed) {
        this.bullet = bullet;
        this.reload = reload;
        this.angularSpeed = angularSpeed;
        this.speed = speed;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (reload < 1) {
            float v = 0;

            while (v < 1) {
                add(game, entity);
                v += reload;
            }

        } else {
            add(game, entity);

            tick++;

            if (tick >= reload) {
                tick = 0;
            }
        }
    }

    private void add(GameScreen game, Entity entity) {
        Vector2 vec = new Vector2(1, 0).rotateDeg(degrees).scl(speed);

        Stage s = game.getStage();
        game.addBullet(s.createBullet(bullet, game, false, new LinearPattern(vec), entity.getPos().cpy()));

        degrees += angularSpeed;

        if (degrees >= 360) {
            degrees -= 360;
        }
    }
}
