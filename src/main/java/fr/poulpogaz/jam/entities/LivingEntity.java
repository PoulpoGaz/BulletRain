package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.particles.AnimatedParticle;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.AnimationDescriptor;
import fr.poulpogaz.jam.utils.Animations;
import fr.poulpogaz.jam.utils.Mathf;
import org.joml.Vector2f;

public abstract class LivingEntity extends Entity {

    private static final AnimationDescriptor HIT = Animations.get("hit_animation");
    private static final AnimationDescriptor DEATH = Animations.get("expl_08_animation");

    protected int life;
    protected boolean hit;

    protected AnimatedParticle death;

    public LivingEntity(Game game) {
        super(game);
    }

    public LivingEntity(Game game, EntityRenderer renderer) {
        super(game, renderer);
    }

    public void hit(Bullet b) {
        if (isAlive()) {
            life -= b.getDamage();

            float x = Mathf.random(getX(), b.getX());
            float y = Mathf.random(getY(), b.getY());
            Vector2f pos = new Vector2f(x, y);

            if (life <= 0) {
                life = 0;

                death = new AnimatedParticle(pos, DEATH);
                particles.add(death);
            } else {
                particles.add(new AnimatedParticle(pos, HIT));
            }

            hit = true;
        }
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
