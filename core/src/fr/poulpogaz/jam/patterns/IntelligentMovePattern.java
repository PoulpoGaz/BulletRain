package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.utils.Mathf;

public class IntelligentMovePattern implements MovePattern {

    private final Vector2 baseSpeed;
    private final float maxSpeed;
    private final int maxLife;

    public IntelligentMovePattern(Vector2 baseSpeed, float maxSpeed, int maxLife) {
        this.baseSpeed = baseSpeed;
        this.maxSpeed = maxSpeed;
        this.maxLife = maxLife;
    }

    @Override
    public void dir(int t, Vector2 dest, GameScreen screen, Entity entity) {
        if (entity instanceof Bullet) {
            Bullet b = (Bullet) entity;

            if (dest.epsilonEquals(0, 0)) {
                dest.set(baseSpeed);

            } else {
                Vector2 dist = screen.getPlayer().getPos().cpy().sub(entity.getPos());

                float fac = 0.2F + 8 * (Mathf.PI - Math.abs(b.getDirection().angleRad(dist))) / (500 * Mathf.PI);

                dest.add(dist.nor().scl(fac * Math.min(5f, dist.len())));

                if (dest.len() >= maxSpeed) {
                    dest.scl(maxSpeed / dest.len());
                }
            }

            if (t >= maxLife) {
                b.setExploded(true);
            }
        }
    }
}
