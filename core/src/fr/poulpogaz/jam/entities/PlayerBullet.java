package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.PlayerBulletDescriptor;

public class PlayerBullet extends Bullet {

    private final float damage;

    public PlayerBullet(GameScreen game, PlayerBulletDescriptor descriptor, boolean playerBullet, MovePattern movePattern, Vector2 pos) {
        super(game, descriptor, playerBullet, movePattern, pos);
        this.damage = game.getPlayer().getPower();
    }

    @Override
    public float getDamage() {
        return damage;
    }
}
