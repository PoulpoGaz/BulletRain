package fr.poulpogaz.jam.particles;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle {

    protected final float lifetime;
    protected float timeAlive;

    protected Vector2 pos;

    public Particle(float lifetime) {
        this.lifetime = lifetime;
    }

    public void update(float delta) {
        timeAlive += delta;
    }

    public abstract void render(SpriteBatch batch, BitmapFont font);

    public Vector2 getPos() {
        return pos;
    }

    public float getLifetime() {
        return lifetime;
    }

    public float getTimeAlive() {
        return timeAlive;
    }

    public float getRemainingTimeAlive() {
        return lifetime - timeAlive;
    }

    public boolean isDead() {
        return lifetime <= timeAlive;
    }
}
