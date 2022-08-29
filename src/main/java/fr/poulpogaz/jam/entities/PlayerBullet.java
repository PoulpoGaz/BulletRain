package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.PlayerBulletDescriptor;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class PlayerBullet extends Bullet {

    private final float damage;

    public PlayerBullet(Game game, PlayerBulletDescriptor descriptor, boolean playerBullet, MovePattern movePattern, Vector2f pos) {
        super(game, descriptor, playerBullet, movePattern, pos);
        this.damage = game.getPlayer().getPower();
    }

    @Override
    public float getDamage() {
        return damage;
    }
}
