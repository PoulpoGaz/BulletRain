package fr.poulpogaz.jam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import fr.poulpogaz.jam.utils.Mathf;

import static fr.poulpogaz.jam.Constants.MAP_HEIGHT;
import static fr.poulpogaz.jam.Constants.MAP_WIDTH;

public interface BackgroundRenderer extends Disposable {

    void loadTextures();

    void renderBackground(float mapScroll, SpriteBatch batch, ShapeRenderer sr);

    class Tex implements BackgroundRenderer {

        protected final String texture;
        protected Texture background;

        public Tex(String texture) {
            this.texture = texture;
        }

        @Override
        public void loadTextures() {
            background = Jam.getOrLoadTexture(texture);
        }

        @Override
        public void renderBackground(float mapScroll, SpriteBatch spriteBatch, ShapeRenderer sr) {
            setTexture();

            float texY = Mathf.absMod(mapScroll, background.getHeight());

            if (texY + MAP_HEIGHT >= background.getHeight()) {
                float u = 0;
                float v = texY / background.getHeight();
                float u2 = 1;
                float v2 = 1;

                float firstPartH = background.getHeight() - texY;
                spriteBatch.draw(background, 0, 0, MAP_WIDTH, firstPartH, u, v, u2, v2);

                float secondPartH = MAP_HEIGHT - firstPartH;
                u = 0;
                v = 0;
                u2 = 1;
                v2 = secondPartH / background.getHeight();

                spriteBatch.draw(background, 0, firstPartH, MAP_WIDTH, secondPartH, u, v, u2, v2);
            } else {
                float u = 0;
                float v = texY / background.getHeight();
                float u2 = 1;
                float v2 = (texY + MAP_HEIGHT) / background.getHeight();

                spriteBatch.draw(background, 0, 0, MAP_WIDTH, MAP_HEIGHT, u, v, u2, v2);
            }
        }

        protected void setTexture() {
            if (background == null) {
                background = Jam.getTexture(texture);
            }
        }

        @Override
        public void dispose() {

        }
    }
}
