package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.Disposable;
import org.joml.Vector4f;

public abstract class State implements Disposable {

    protected final StateManager manager = StateManager.getInstance();

    protected Vector4f clearColor;

    public State() {

    }

    public void show() throws Exception {

    }

    public void render(Graphics2D g2d, FontRenderer fr) {
        renderBackground(g2d, fr);
        renderForeground(g2d, fr);
    }

    protected void renderForeground(Graphics2D g2d, FontRenderer fr) {

    }

    protected void renderBackground(Graphics2D g2d, FontRenderer fr) {

    }

    public void update(float delta) {

    }

    public void hide() {

    }

    public Vector4f getClearColor() {
        return clearColor;
    }
}