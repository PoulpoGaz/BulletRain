package fr.poulpogaz.jam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public interface Effect extends Disposable {

    void loadTextures();

    void reset();

    void renderEffect(SpriteBatch batch, ShapeRenderer sr);

    Effect NONE = new Effect() {
        @Override
        public void dispose() {

        }

        @Override
        public void loadTextures() {

        }

        @Override
        public void reset() {

        }

        @Override
        public void renderEffect(SpriteBatch batch, ShapeRenderer sr) {

        }
    };
}
