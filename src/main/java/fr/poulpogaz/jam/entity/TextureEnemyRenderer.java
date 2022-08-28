package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class TextureEnemyRenderer implements EnemyRenderer {

    private final String texture;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    private ITexture tex;

    public TextureEnemyRenderer(String texture) {
        this(texture, -1, -1, -1, -1);
    }

    public TextureEnemyRenderer(String texture, int x, int y, int w, int h) {
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
    public void render(Graphics2D g2d, FontRenderer f2d, Game game, Enemy enemy) {
        Vector2f p = enemy.getPos();

        g2d.drawSprite(tex, p.x - tex.getWidth() / 2f, p.y - tex.getHeight() / 2f);
    }
}
