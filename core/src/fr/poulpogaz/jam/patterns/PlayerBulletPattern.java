package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.AbstractEnemy;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.entities.Player;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

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

    private static final Vector2 BULLET_DIR = new Vector2(0, 1).nor().scl(PLAYER_BULLET_SPEED);
    private static final Vector2 BULLET_DIR_B = new Vector2(0, 1).rotateDeg(5).nor().scl(PLAYER_BULLET_SPEED);
    private static final Vector2 BULLET_DIR_B2 = new Vector2(0, 1).rotateDeg(-5).nor().scl(PLAYER_BULLET_SPEED);

    private static final LinearPattern PATTERN_A = new LinearPattern(BULLET_DIR);
    private static final LinearPattern PATTERN_B = new LinearPattern(BULLET_DIR_B);
    private static final LinearPattern PATTERN_B2 = new LinearPattern(BULLET_DIR_B2);

    private final Player player;
    private int tickCount = 0;

    public PlayerBulletPattern(Player player) {
        this.player = player;
    }

    @Override
    public void reset() {
        tickCount = 0;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean p) {
        Stage s = game.getStage();
        Vector2 pos = player.getPos();

        if (tickCount % 5 == 0) {
            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add( 5, 5)));
            game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-5, 5)));

            if (player.getPower() >= 2) {
                if (player.isSlowdown()) {
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(8, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-8, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(11, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-11, 5)));
                } else {
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_B2, pos.cpy().add(8, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_B, pos.cpy().add(-8, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_B2, pos.cpy().add(11, 5)));
                    game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_B, pos.cpy().add(-11, 5)));
                }
            }
        }


        if (player.getPower() >= 3) {
            if (player.isSlowdown() && tickCount % 5 == 0) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(1, 12)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(3, 12)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-3, 12)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, PATTERN_A, pos.cpy().add(-1, 12)));


            } else if (tickCount % 10 == 0) {
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(new Vector2( 1,  1).nor().scl(PLAYER_BULLET_SPEED)), pos.cpy().add(13, 0)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(new Vector2(-1,  1).nor().scl(PLAYER_BULLET_SPEED)), pos.cpy().add(13, -3)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(new Vector2(-1, -1).nor().scl(PLAYER_BULLET_SPEED)), pos.cpy().add(-13, -3)));
                game.addBullet(s.createPlayerBullet(PLAYER_BULLET_NAME, game, new Level3BulletMovePattern(new Vector2( 1, -1).nor().scl(PLAYER_BULLET_SPEED)), pos.cpy().add(-13, 0)));
            }
        }

        tickCount++;
        if (tickCount >= 10) {
            tickCount = 0;
        }
    }

    private static class Level3BulletMovePattern implements MovePattern {

        private final Vector2 baseSpeed;
        private AbstractEnemy target;

        public Level3BulletMovePattern(Vector2 baseSpeed) {
            this.baseSpeed = baseSpeed;
        }

        @Override
        public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
            if (entity instanceof Bullet) {
                Bullet b = (Bullet) entity;

                if (target == null || target.isDead()) {
                    target = screen.nearestEnemy(b);
                }

                if (dest.epsilonEquals(0, 0)) {
                    dest.set(baseSpeed);

                } else if (target != null) {
                    Vector2 dist = target.getPos().cpy().sub(entity.getPos());

                    float fac = 0.2F + 8 * (Mathf.PI - Math.abs(b.getDirection().angleRad(dist))) / (10 * Mathf.PI);

                    dest.add(dist.nor().scl(fac * Math.min(5f, dist.len())));

                    if (dest.len() >= PLAYER_BULLET_SPEED) {
                        dest.scl(PLAYER_BULLET_SPEED / dest.len());
                    }
                }
            }
        }
    }
}
