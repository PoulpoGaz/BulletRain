package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.entity.Bullet;
import fr.poulpogaz.jam.entity.Entity;
import fr.poulpogaz.jam.entity.Player;
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

    private final Player player;
    private int tickCount = 0;

    public PlayerBulletPattern(Player player) {
        this.player = player;
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
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos1));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-5, -5);
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos2));
    }

    private void level2Shots(Game game) {
        Vector2f pos1 = new Vector2f(player.getPos());
        pos1.add(8, -5);
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos1));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-8, -5);
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos2));

        Vector2f pos3 = new Vector2f(player.getPos());
        pos3.add(11, -5);
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos3));

        Vector2f pos4 = new Vector2f(player.getPos());
        pos4.add(-11, -5);
        game.addBullet(new Bullet(game, true, new LinearPattern(BULLET_DIR), pos4));
    }

    private void level3Shots(Game game) {
        Vector2f pos1 = new Vector2f(player.getPos());
        pos1.add(13, 0);
        game.addBullet(new Bullet(game, true, new Level3BulletMovePattern(false, 0.05f), pos1));

        Vector2f pos2 = new Vector2f(player.getPos());
        pos2.add(-13, 0);
        game.addBullet(new Bullet(game, true, new Level3BulletMovePattern(true, 0.05f), pos2));

        Vector2f pos3 = new Vector2f(player.getPos());
        pos3.add(13, 3);
        game.addBullet(new Bullet(game, true, new Level3BulletMovePattern(false, 0.1f), pos3));

        Vector2f pos4 = new Vector2f(player.getPos());
        pos4.add(-13, 3);
        game.addBullet(new Bullet(game, true, new Level3BulletMovePattern(true, 0.1f), pos4));
    }

    private static class Level3BulletMovePattern implements MovePattern {

        private final boolean left;
        private final float length;

        public Level3BulletMovePattern(boolean left, float length) {
            this.left = left;
            this.length = length;
        }

        @Override
        public Vector2f dir(float t) {
            if (t <= length) {
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
