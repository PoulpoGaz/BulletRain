package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;

public class CircularMove extends AbstractMovePattern {

    private final Vector2 relativeCenter;
    private final float speed;
    private final float radius;

    private final Vector2 center = new Vector2();
    private final Vector2 uTheta = new Vector2();

    public CircularMove(Vector2 relativeCenter, float speed) {
        this.relativeCenter = relativeCenter;
        this.speed = speed;
        this.radius = relativeCenter.len();
    }

    @Override
    public void init(GameScreen game, Entity entity, int tStart) {
        super.init(game, entity, tStart);
        center.set(entity.getPos()).add(relativeCenter);
        uTheta.set(entity.getPos()).sub(center).nor().rotateRad(Mathf.PI_2);
    }

    @Override
    public void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
        // length of an arc = theta * radius
        float theta = t * speed / radius;

        last.set(uTheta).rotateRad(theta).scl(speed);
    }
}
