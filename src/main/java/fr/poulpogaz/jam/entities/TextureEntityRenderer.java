package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class TextureEntityRenderer implements EntityRenderer {

    protected final String texture;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;

    protected ITexture tex;

    public TextureEntityRenderer(String texture) {
        this(texture, -1, -1, -1, -1);
    }

    public TextureEntityRenderer(String texture, int x, int y, int w, int h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void loadTextures() throws Exception {
        Texture t = TextureCache.getOrCreate(texture);

        if (x >= 0) {
            tex = new SubTexture(x, y, w, h, t);
        } else {
            tex = t;
        }
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d, Game game, Entity entity) {
        Vector2f p = entity.getPos();

        g2d.drawSprite(tex, p.x - tex.getWidth() / 2f, p.y - tex.getHeight() / 2f);
    }
}
