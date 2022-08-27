package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

public class Bullet extends Entity {

    private static final Logger LOGGER = LogManager.getLogger(Bullet.class);

    private final boolean playerBullet;
    private final MovePattern movePattern;

    private float t = 0;
    private Vector2f dir;

    private ITexture tex;

    public Bullet(Game game, boolean playerBullet, MovePattern movePattern, Vector2f pos) {
        super(game);
        this.pos = pos;
        this.playerBullet = playerBullet;
        this.movePattern = movePattern;

        dir = movePattern.dir(t);

        Texture t = TextureCache.get("textures/tileset.png");
        tex = new SubTexture(32, 96, 10, 3, t);

        LOGGER.debug("Creating bullet at {}. Direction: {}", pos, dir);
    }

    @Override
    public void update(Input in, float delta) {
        dir = movePattern.dir(t);
        pos.add(dir);

        t += GameEngine.INV_TPS;
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        float angle = (float) (Math.atan2(dir.y, dir.x));

        g2d.translate(pos.x, pos.y);
        g2d.rotate(angle);
        g2d.drawSprite(tex, -tex.getWidth() / 2f, -tex.getHeight() / 2f);
        g2d.rotate(-angle);
        g2d.translate(-pos.x, -pos.y);

        //g2d.drawSprite(tex, pos.x - tex.getWidth() / 2f, pos.y - tex.getHeight() / 2f);
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }
}
