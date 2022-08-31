package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;

public class TargetPlayer implements BulletPattern {

    private final String bullet;
    private final int reloadTick;
    private int tick;

    public TargetPlayer(String bullet, int reloadTick) {
        this.bullet = bullet;
        this.reloadTick = reloadTick;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (tick == 0) {
            Vector2 playerPos = game.getPlayer().getPos();
            Vector2 dir = playerPos.cpy().sub(entity.getPos()).nor().scl(3);

            Stage s = game.getStage();
            game.addBullet(s.createBullet(bullet, game, false, new LinearPattern(dir), entity.getPos().cpy()));
        }

        tick++;
        if (tick >= reloadTick) {
            tick = 0;
        }
    }
}
