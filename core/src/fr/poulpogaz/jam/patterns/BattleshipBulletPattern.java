package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Battleship;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;

public class BattleshipBulletPattern implements BulletPattern {

    private final String bullet;
    private int tick;

    public BattleshipBulletPattern(String bullet) {
        this.bullet = bullet;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        Battleship ship = (Battleship) entity;

        if (tick % 10 == 0 && tick < 120) {
            fireTurret(ship.getTurret1Pos(), game);
            fireTurret(ship.getTurret2Pos(), game);
        }

        tick++;
        if (tick >= 180) {
            tick = 0;
        }
    }

    private void fireTurret(Vector2 turret, GameScreen screen) {
        Vector2 dir = screen.getPlayer().getPos().cpy().sub(turret).nor().scl(5f);
        float angle = dir.angleRad();

        Vector2 pos = new Vector2().set(5, 0).rotateRad(angle).add(turret);

        Stage s = screen.getStage();
        screen.addBullet(s.createBullet(bullet, screen, false, new LinearPattern(dir), pos));
    }
}
