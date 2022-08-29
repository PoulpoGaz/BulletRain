package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entity.BasicBullet;
import fr.poulpogaz.jam.entity.Entity;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class TargetPlayer implements BulletPattern {

    private final int reloadTick;
    private int tick;

    public TargetPlayer(int reloadTick) {
        this.reloadTick = reloadTick;
    }

    @Override
    public void addBullets(Game game, Entity entity, boolean player) {
        if (tick == 0) {
            Vector2f playerPos = game.getPlayer().getPos();
            Vector2f dir = playerPos.sub(entity.getPos(), new Vector2f()).normalize(5);

            ITexture t = new SubTexture(32, 96, 10, 3, TextureCache.get("tileset.png"));

            game.addBullet(new BasicBullet(game, player, new LinearPattern(dir), new Vector2f(entity.getPos()), t, 5));
        }

        tick++;
        if (tick >= reloadTick) {
            tick = 0;
        }
    }
}
