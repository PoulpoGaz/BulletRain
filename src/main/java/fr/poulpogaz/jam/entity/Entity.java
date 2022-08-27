package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;

public abstract class Entity {

    protected int x;
    protected int y;

    protected int vx;
    protected int vy;

    public abstract void update(Input in, float delta);

    public abstract void render(Graphics2D g2d, FontRenderer f2d);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }
}
