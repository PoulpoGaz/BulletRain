package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.particles.AnimatedParticle;
import fr.poulpogaz.jam.utils.Animations;
import fr.poulpogaz.jam.utils.Mathf;

public abstract class LivingEntity extends Entity {

    private static Animation<TextureRegion> HIT;
    private static Animation<TextureRegion> DEATH;

    protected int life;
    protected int maxLife;
    protected boolean hit;

    protected AnimatedParticle death;

    public LivingEntity(GameScreen game) {
        super(game);
    }

    public LivingEntity(GameScreen game, EntityRenderer renderer) {
        super(game, renderer);
    }

    public void hit(Bullet b) {
        if (isAlive()) {
            life -= b.getDamage();

            float x = Mathf.random(getX(), b.getX());
            float y = Mathf.random(getY(), b.getY());
            Vector2 pos = new Vector2(x, y);

            if (life <= 0) {
                life = 0;

                if (DEATH == null) {
                    DEATH = Animations.get("expl_08");
                }

                death = new AnimatedParticle(pos, DEATH);
                particles.add(death);
            } else {
                if (HIT == null) {
                    HIT = Animations.get("hit");
                }

                particles.add(new AnimatedParticle(pos, HIT));
            }

            hit = true;
        }
    }

    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public boolean isDying() {
        return death != null && !death.isDead();
    }

    public float percentToDeath() {
        if (death == null) {
            return 0;
        }

        return (float) death.getTimeAlive() / death.getLifetime();
    }

    public boolean isAlive() {
        return life > 0;
    }

    public boolean isHit() {
        return hit;
    }
}
