package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public interface BulletFactory {

    Bullet createBullet(String name, Game game, boolean playerBullet, MovePattern pattern, Vector2f pos);
}
