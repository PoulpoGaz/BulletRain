package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.states.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

public abstract class TextureBullet extends AbstractBullet {

    private static final Logger LOGGER = LogManager.getLogger(TextureBullet.class);

    protected final ITexture texture;

    public TextureBullet(Game game,
                         boolean playerBullet,
                         MovePattern movePattern,
                         Vector2f pos,
                         ITexture texture,
                         float damage) {
        super(game, playerBullet, movePattern, pos, damage);
        this.texture = texture;
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        super.render(g2d, f2d);

        g2d.translate(pos.x, pos.y);
        g2d.rotate(angle);
        g2d.drawSprite(texture, -texture.getWidth() / 2f, -texture.getHeight() / 2f);
        g2d.rotate(-angle);
        g2d.translate(-pos.x, -pos.y);
    }
}
