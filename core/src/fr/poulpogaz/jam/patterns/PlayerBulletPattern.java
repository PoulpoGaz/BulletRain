package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.entities.Player;
import fr.poulpogaz.jam.stage.Stage;

import static fr.poulpogaz.jam.Constants.PLAYER_BULLET_NAME;
import static fr.poulpogaz.jam.Constants.PLAYER_BULLET_SPEED;

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

    private static final Vector2 BULLET_DIR = new Vector2(0, 1).scl(PLAYER_BULLET_SPEED);
    private static final LinearPattern PATTERN_A = new LinearPattern(BULLET_DIR);

    private final Player player;
    private int tickCount = 0;

    public PlayerBulletPattern(Player player) {
        this.player = player;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean p) {

        if (tickCount == 0) {
            Stage s = game.getStage();
            Vector2 pos = player.getPos();

            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add( 5, 5)));
            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-5, 5)));

            if (player.getPower() >= 2) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(  8, 5)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add( -8, 5)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add( 11, 5)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-11, 5)));
            }

            if (player.getPower() >= 3) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(false, 5), pos.cpy().add( 13, 0)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(true, 5), pos.cpy().add(-13, 0)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(false, 10), pos.cpy().add( 13, -3)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(true, 10), pos.cpy().add(-13, -3)));
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
        public Vector2 dir(int t, Vector2 dest) {
            if (t < length) {
                if (left) {
                    dest.set(-1, 1).nor().scl(PLAYER_BULLET_SPEED);
                } else {
                    dest.set(1, 1).nor().scl(PLAYER_BULLET_SPEED);
                }
            } else {
                dest.set(BULLET_DIR);
            }

            return dest;
        }
    }
}