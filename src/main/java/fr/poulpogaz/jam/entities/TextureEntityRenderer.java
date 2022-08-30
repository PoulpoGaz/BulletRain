package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.renderer.Color;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

public class TextureEntityRenderer implements EntityRenderer {

    private static final Logger LOGGER = LogManager.getLogger(TextureEntityRenderer.class);

    protected final String texture;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;

    protected ITexture tex;

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
    public void loadTextures() throws Exception {
        Texture t = TextureCache.getOrCreate(texture);

        if (t == null) {
            throw new RuntimeException("Can't find texture: " + texture);
        }

        if (w >= 0 || h >= 0) {
            tex = new SubTexture(x, y, w, h, t);
        } else {
            tex = t;
        }
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d, Game game, Entity entity) {
        Vector2f p = entity.getPos();

        if (entity instanceof LivingEntity l && l.isDying()) {
            g2d.drawSprite(tex, new Color(1, 1, 1, 1 - l.percentToDeath()), p.x - tex.getWidth() / 2f, p.y - tex.getHeight() / 2f);
        } else {
            g2d.drawSprite(tex, p.x - tex.getWidth() / 2f, p.y - tex.getHeight() / 2f);
        }
    }
}
