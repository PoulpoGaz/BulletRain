package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.stage.EnemyScript;

public class Battleship extends Enemy {

    private static final Vector2 TURRET_1 = new Vector2(20, 0);
    private static final Vector2 TURRET_2 = new Vector2(-28, 0);

    private final Vector2 turret1Pos = new Vector2();
    private final Vector2 turret2Pos = new Vector2();

    public Battleship(GameScreen game, EnemyScript script) {
        super(game, script);
    }

    @Override
    protected void onMove() {
        turret1Pos.set(TURRET_1).rotateDeg(angleDeg).add(getPos());
        turret2Pos.set(TURRET_2).rotateDeg(angleDeg).add(getPos());
    }

    public Vector2 getTurret1Pos() {
        return turret1Pos;
    }

    public Vector2 getTurret2Pos() {
        return turret2Pos;
    }
}
