package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.particles.AnimatedParticle;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.AnimationDescriptor;
import fr.poulpogaz.jam.utils.Animations;

public abstract class LivingEntity extends Entity {

    private static final AnimationDescriptor HIT = Animations.get("hit_animation");

    protected int life;
    protected boolean hit;

    public LivingEntity(Game game) {
        super(game);
    }

    public LivingEntity(Game game, EntityRenderer renderer) {
        super(game, renderer);
    }

    public void hit(Bullet b) {
        if (life > 0) {
            //life -= b.getDamage();

            if (life <= 0) {
                life = 0;
            } else {
                game.addParticle(new AnimatedParticle(b.getPos(), HIT));
            }

            hit = true;
        }
    }

    public int getLife() {
        return life;
    }

    public boolean isDied() {
        return life <= 0;
    }

    public boolean isHit() {
        return hit;
    }
}
