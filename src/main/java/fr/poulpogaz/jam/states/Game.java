package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.entity.*;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.utils.GLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends State {

    private static final Logger LOGGER = LogManager.getLogger(Game.class);

    private final Player player;
    private final List<Enemy> enemies;
    private final List<AbstractBullet> playerBullets;
    private final List<AbstractBullet> enemiesBullets;

    private BasicBullet myBullet;

    private final AABB screen = new AABB(0, 0, Jam.WIDTH, Jam.HEIGHT);
    private final AABB largeScreen = new AABB(-50, -50, Jam.WIDTH + 100, Jam.HEIGHT + 100);

    private ITexture background;
    private int mapY;

    public Game() {
        player = new Player(this, Jam.WIDTH / 2, Jam.HEIGHT / 2);
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemiesBullets = new ArrayList<>();

        Texture t = TextureCache.get("tileset.png");
        ITexture texture = new SubTexture(32, 96, 10, 3, t);
        myBullet = new BasicBullet(this, true, (tick) -> {
            float v = tick * GameEngine.INV_TPS;

            float vx = Math.cos(v);
            float vy = Math.sin(v);

            return new Vector2f(vx, vy);
        }, new Vector2f(player.getPos()), texture, 0);

        try {
            background = TextureCache.getOrCreate("desert_background.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void renderBackground(Graphics2D g2d, FontRenderer f2d) {
        int texY = background.getHeight() - mapY % background.getHeight() - Jam.HEIGHT;

        if (texY < 0) {
            int texY2 = background.getHeight() + texY;
            g2d.drawSprite(background, 0, 0, Jam.WIDTH, -texY, 0, texY2, background.getWidth(), -texY);
            g2d.drawSprite(background, 0, -texY, Jam.WIDTH, Jam.HEIGHT + texY, 0, 0, background.getWidth(), Jam.HEIGHT + texY);
        } else {
            g2d.drawSprite(background, 0, 0, Jam.WIDTH, Jam.HEIGHT, 0, texY, background.getWidth(), Jam.HEIGHT);
        }
    }

    @Override
    protected void renderForeground(Graphics2D g2d, FontRenderer f2d) {
        GLUtils.blend();
        player.render(g2d, f2d);

        for (AbstractBullet p : playerBullets) {
            AABB aabb = p.aabb();

            if (aabb.collide(screen).intersect()) {
                p.render(g2d, f2d);
            }
        }
        myBullet.render(g2d, f2d);

        if (Constants.DEBUG) {
            f2d.drawString("Player: (%d, %d)".formatted((int) player.getX(), (int) player.getY()),
                    0, 0);
            f2d.drawString("Number of bullets: %d".formatted(playerBullets.size()),
                    0, f2d.getFont().getHeight());
        }
        GLUtils.noBlend();
    }

    @Override
    public void update(float delta) {
        player.update(input, delta);

        for (int i = 0; i < playerBullets.size(); i++) {
            AbstractBullet p = playerBullets.get(i);

            if (largeScreen.collide(p.aabb()).intersect()) {
                p.update(input, delta);
            } else {
                // remove, bullet is out of the screen and
                // there is practically no chance the bullet will one day come back
                playerBullets.remove(i);
                i--;
            }
        }

        myBullet.update(input, delta);

        mapY++;
    }

    @Override
    public void dispose() {

    }

    public void addBullet(AbstractBullet bullet) {
        if (bullet.isPlayerBullet()) {
            playerBullets.add(bullet);
        } else {
            enemiesBullets.add(bullet);
        }
    }
}
