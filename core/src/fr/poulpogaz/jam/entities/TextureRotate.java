package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.poulpogaz.jam.GameScreen;

public class TextureRotate extends TextureEntityRenderer {

    public TextureRotate(String texture) {
        super(texture);
    }

    public TextureRotate(String texture, int x, int y, int w, int h) {
        super(texture, x, y, w, h);
    }


    @Override
    public void render(SpriteBatch batch, BitmapFont f2d, GameScreen game, Entity entity) {
        setTexture();

        if (entity instanceof IRotateEntity) {
            IRotateEntity e = (IRotateEntity) entity;

            batch.draw(tex,
                    entity.getX() - tex.getRegionWidth() / 2f, entity.getY() - tex.getRegionHeight() / 2f,
                    tex.getRegionWidth() / 2f, tex.getRegionHeight() / 2f, tex.getRegionWidth(), tex.getRegionHeight(),
                    1, 1,
                    e.getAngleDeg());
        }
    }
}
