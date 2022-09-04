package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Battleship;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;

import java.util.function.Function;

public class BattleshipBulletPattern implements BulletPattern {

    private final String bullet1;
    private final String bullet2;
    private int tick;

    public BattleshipBulletPattern(String bullet1, String bullet2) {
        this.bullet1 = bullet1;
        this.bullet2 = bullet2;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        Battleship ship = (Battleship) entity;

        if (tick % 10 == 0 && tick < 120) {
            fireTurret(bullet1, ship.getTurret1Pos(), game, LinearPattern::new);
        }

        if (tick % 15 == 0 && tick > 130 && tick < 230) {
            fireTurret(bullet2, ship.getTurret2Pos(), game, (p) -> new IntelligentMovePattern(p, 4f, 60 * 4));
        }

        tick++;
        if (tick >= 230 + 10 + 60 * 4) {
            tick = 0;
        }
    }

    private void fireTurret(String bullet, Vector2 turret, GameScreen screen, Function<Vector2, MovePattern> pattern) {
        Vector2 dir = screen.getPlayer().getPos().cpy().sub(turret).nor().scl(5f);
        float angle = dir.angleRad();

        Vector2 pos = new Vector2().set(5, 0).rotateRad(angle).add(turret);

        Stage s = screen.getStage();
        screen.addBullet(s.createBullet(bullet, screen, false, pattern.apply(dir), pos));
    }
}
