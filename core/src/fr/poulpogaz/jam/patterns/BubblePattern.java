package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

public class BubblePattern implements BulletPattern {

    private final String bullet;
    private final float speed;
    private int tick;

    public BubblePattern(String bullet, float speed) {
        this.bullet = bullet;
        this.speed = speed;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (tick < 10) {
            Stage stage = game.getStage();

            Vector2 dir = game.getPlayer().getPos().cpy().sub(entity.getPos());
            dir.nor().rotateDeg(Mathf.random(-5, 5)).scl(speed);

            game.addBullet(stage.createBullet(bullet, game, false, new LinearPattern(dir), entity.getPos().cpy()));
        }


        tick++;
        if (tick >= 30) {
            tick = 0;
        }
    }
}
