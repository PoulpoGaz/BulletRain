package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.entities.Player;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import static fr.poulpogaz.jam.Constants.PLAYER_BULLET_NAME;

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
    private static final LinearPattern PATTERN_A = new LinearPattern(BULLET_DIR);

    private static ITexture texture;

    private final Player player;
    private int tickCount = 0;

    public PlayerBulletPattern(Player player) {
        this.player = player;

        if (texture == null) {
            Texture t = TextureCache.get("tileset.png");
            texture = new SubTexture(32, 96, 10, 3, t);
        }
    }

    @Override
    public void addBullets(Game game, Entity entity, boolean p) {

        if (tickCount == 0) {
            Stage s = game.getStage();
            Vector2f pos = player.getPos();

            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add( 5, -5, new Vector2f())));
            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add(-5, -5, new Vector2f())));

            if (player.getPower() >= 2) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add(  8, -5, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add( -8, -5, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add( 11, -5, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.add(-11, -5, new Vector2f())));
            }

            if (player.getPower() >= 3) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(false, 5), pos.add( 13, 0, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(true, 5), pos.add(-13, 0, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(false, 10), pos.add( 13, 3, new Vector2f())));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(true, 10), pos.add(-13, 3, new Vector2f())));
            }
        }

        tickCount++;
        if (tickCount >= 5) {
            tickCount = 0;
        }
    }

    private static class Level3BulletMovePattern implements MovePattern {

        private final boolean left;
        private final int length;

        public Level3BulletMovePattern(boolean left, int length) {
            this.left = left;
            this.length = length;
        }

        @Override
        public Vector2f dir(int t, Vector2f dest) {
            if (t < length) {
                if (left) {
                    dest.set(-5, -5);
                } else {
                    dest.set(5, -5);
                }
            } else {
                dest.set(BULLET_DIR);
            }

            return dest;
        }
    }
}
