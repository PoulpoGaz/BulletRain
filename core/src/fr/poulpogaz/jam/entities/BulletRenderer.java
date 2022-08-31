package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.poulpogaz.jam.GameScreen;

public class BulletRenderer extends TextureEntityRenderer {

    public BulletRenderer(String texture) {
        super(texture);
    }

    public BulletRenderer(String texture, int x, int y, int w, int h) {
        super(texture, x, y, w, h);
    }


    @Override
    public void render(SpriteBatch batch, BitmapFont f2d, GameScreen game, Entity entity) {
        setTexture();

        if (entity instanceof Bullet bullet) {

            /*batch.draw(tex,
                    bullet.getX() - tex.getRegionWidth() / 2f, bullet.getY() - tex.getRegionWidth() / 2f,
                    0, 0, tex.getRegionWidth(), tex.getRegionHeight(),
                    1, 1,
                    bullet.getAngleDeg());*/

            batch.draw(tex,
                    bullet.getX() - tex.getRegionWidth() / 2f, bullet.getY() - tex.getRegionHeight() / 2f,
                    tex.getRegionWidth() / 2f, tex.getRegionHeight() / 2f, tex.getRegionWidth(), tex.getRegionHeight(),
                    1, 1,
                    bullet.getAngleDeg());
        }
    }
}
