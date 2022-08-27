package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import java.util.Objects;

public abstract class Entity {

    protected final Game game;
    protected Vector2f pos;

    public Entity(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    public abstract void update(Input in, float delta);

    public abstract void render(Graphics2D g2d, FontRenderer f2d);

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public Vector2f getPos() {
        return pos;
    }
}
