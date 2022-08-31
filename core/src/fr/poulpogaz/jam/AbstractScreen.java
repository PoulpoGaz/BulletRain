package fr.poulpogaz.jam;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class AbstractScreen implements Screen {

    protected final Jam jam;

    protected BitmapFont font;
    protected SpriteBatch spriteBatch;
    protected ShapeRenderer shapeRenderer;

    public AbstractScreen(Jam jam) {
        this.jam = jam;
    }

    /**
     * Load textures with the global assets manager {@link Jam#manager}
     */
    public abstract void preLoad();

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch sprite) {
        this.spriteBatch = sprite;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setShapeRenderer(ShapeRenderer shape) {
        this.shapeRenderer = shape;
    }
}
