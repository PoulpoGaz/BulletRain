package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entity.BasicBullet;
import fr.poulpogaz.jam.entity.TextureBullet;
import fr.poulpogaz.jam.entity.Entity;
import fr.poulpogaz.jam.entity.Player;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

/**
 * Power system:
 * power < 2: two perpendicular shot
 * power < 3: six perpendicular shot
 * power < 4: six perpendicular shot + four partial perpendicular shot (moving in diagonal and then perpendicular)
 *
 * Player fire every 5 ticks
 * Power also increase damage
 */
public class PlayerBulletPattern implements BulletPattern {

    private static final Vector2f BULLET_DIR = new Vector2f(0, -5);

    private static ITexture texture;

    private final Player player;
    private int tickCount = 0;

    public PlayerBulletPattern(Player player) {
        this.player = player;

        if (texture == null) {
            Texture t = TextureCache.get("textures/tileset.png");
            texture = new SubTexture(32, 96, 10, 3, t);
        }
    }

    @Override
    public void addBullets(Game game, Entity entity, boolean p) {

        if (tickCount == 0) {
            level1Shots(game);

            if (player.getPower() >= 2) {
                level2Shots(game);
            }

            if (player.getPower() >= 3) {
                level3Shots(game);
            }
        }

        tickCount++;
        if (tickCount >= 5) {
            tickCount = 0;
        }
    }

    private void level1Shots(Game game) {
        Vector2f pos1 = new Vector2f(player.getPos());
        pos1.add(5, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos1, texture));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-5, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos2, texture));
    }

    private void level2Shots(Game game) {
        Vector2f pos1 = new Vector2f(player.getPos());
        pos1.add(8, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos1, texture));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-8, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos2, texture));

        Vector2f pos3 = new Vector2f(player.getPos());
        pos3.add(11, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos3, texture));

        Vector2f pos4 = new Vector2f(player.getPos());
        pos4.add(-11, -5);
        game.addBullet(new BasicBullet(game, true, new LinearPattern(BULLET_DIR), pos4, texture));
    }

    private void level3Shots(Game game) {
        Vector2f pos1 = new Vector2f(player.getPos());
        pos1.add(13, 0);
        game.addBullet(new BasicBullet(game, true, new Level3BulletMovePattern(false, 5), pos1, texture));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-13, 0);
        game.addBullet(new BasicBullet(game, true, new Level3BulletMovePattern(true, 5), pos2, texture));

        Vector2f pos3 = new Vector2f(player.getPos());
        pos3.add(13, 3);
        game.addBullet(new BasicBullet(game, true, new Level3BulletMovePattern(false, 10), pos3, texture));

        Vector2f pos4 = new Vector2f(player.getPos());
        pos4.add(-13, 3);
        game.addBullet(new BasicBullet(game, true, new Level3BulletMovePattern(true, 10), pos4, texture));
    }

    private static class Level3BulletMovePattern implements MovePattern {

        private final boolean left;
        private final int length;

        public Level3BulletMovePattern(boolean left, int length) {
            this.left = left;
            this.length = length;
        }

        @Override
        public Vector2f dir(int t) {
            if (t < length) {
                if (left) {
                    return new Vector2f(-5, -5);
                } else {
                    return new Vector2f(5, -5);
                }
            } else {
                return BULLET_DIR;
            }
        }
    }
}
