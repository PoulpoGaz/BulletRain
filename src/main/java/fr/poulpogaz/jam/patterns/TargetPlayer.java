package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class TargetPlayer implements BulletPattern {

    private final String bullet;
    private final int reloadTick;
    private int tick;

    public TargetPlayer(String bullet, int reloadTick) {
        this.bullet = bullet;
        this.reloadTick = reloadTick;
    }

    @Override
    public void addBullets(Game game, Entity entity, boolean player) {
        if (tick == 0) {
            Vector2f playerPos = game.getPlayer().getPos();
            Vector2f dir = playerPos.sub(entity.getPos()).normalize(3);

            Stage s = game.getStage();
            game.addBullet(s.createBullet(bullet, game, false, new LinearPattern(dir), entity.getPos()));
        }

        tick++;
        if (tick >= reloadTick) {
            tick = 0;
        }
    }
}
