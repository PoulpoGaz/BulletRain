package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.poulpogaz.jam.GameScreen;

public class TextureRotate extends TextureEntityRenderer {

    private float cx = -1;
    private float cy = -1;

    public TextureRotate(String texture) {
        super(texture);
    }

    public TextureRotate(String texture, int x, int y, int w, int h) {
        super(texture, x, y, w, h);
    }

    public TextureRotate(String texture, float cx, float cy) {
        super(texture);
        this.cx = cx;
        this.cy = cy;
    }

    public TextureRotate(String texture, int x, int y, int w, int h, float cx, float cy) {
        super(texture, x, y, w, h);
        this.cx = cx;
        this.cy = cy;
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont f2d, GameScreen game, Entity entity) {
        setTexture();

        if (entity instanceof IRotateEntity) {
            IRotateEntity e = (IRotateEntity) entity;

            float cx;
            float cy;
            if (this.cx >= 0 && this.cy >= 0) {
                cx = this.cx;
                cy = this.cy;
            } else {
                cx = tex.getRegionWidth() / 2f;
                cy = tex.getRegionHeight() / 2f;

            }
            batch.draw(tex,
                    entity.getX() - cx, entity.getY() - cy,
                    cx, cy, tex.getRegionWidth(), tex.getRegionHeight(),
                    1, 1,
                    e.getAngleDeg());
        }
    }
}
