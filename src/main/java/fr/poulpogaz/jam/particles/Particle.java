package fr.poulpogaz.jam.particles;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import org.joml.Vector2f;

public abstract class Particle {

    protected final int lifetime;
    protected int timeAlive;

    protected Vector2f pos;

    public Particle(int lifetime) {
        this.lifetime = lifetime;
    }

    public void update(float delta) {
        timeAlive++;
    }

    public abstract void render(Graphics2D g2d, FontRenderer f2d);

    public Vector2f getPos() {
        return getPos(new Vector2f());
    }

    public Vector2f getPos(Vector2f dest) {
        return dest.set(pos);
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public int getRemainingTimeAlive() {
        return lifetime - timeAlive;
    }

    public boolean isDead() {
        return lifetime <= timeAlive;
    }
}
