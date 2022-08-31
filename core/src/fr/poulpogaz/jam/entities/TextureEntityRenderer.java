package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;

public class TextureEntityRenderer implements EntityRenderer {

    protected final String texture;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;

    protected TextureRegion tex;

    public TextureEntityRenderer(String texture) {
        this(texture, 0, 0, -1, -1);
    }

    public TextureEntityRenderer(String texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void loadTextures() {
        Texture t = Jam.getOrLoadTexture(texture);

        if (t != null) {
            tex = new TextureRegion(t, x, y, w, h);
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
        setTexture();

        Vector2 p = entity.getPos();

        if (entity instanceof LivingEntity) {
            LivingEntity l = (LivingEntity) entity;

            if (l.isDying()) {
                batch.setColor(1, 1, 1, 1 - l.percentToDeath());
                batch.draw(tex, p.x - w / 2f, p.y - h / 2f);
                batch.setColor(1, 1, 1, 1);
                return;
            }
        }

        batch.draw(tex, p.x - w / 2f, p.y - h / 2f);
    }

    protected void setTexture() {
        if (tex == null) {
            Texture t = Jam.getTexture(texture);

            tex = new TextureRegion(t, x, y, w, h);
        }
    }
}
