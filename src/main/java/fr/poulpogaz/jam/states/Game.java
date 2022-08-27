package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.entity.*;
import fr.poulpogaz.jam.patterns.StaticPattern;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import org.joml.Math;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Game extends State {

    private final Player player;
    private final List<Enemy> enemies;
    private final List<AbstractBullet> playerBullets;
    private final List<AbstractBullet> enemiesBullets;

    private BasicBullet myBullet;

    private final AABB screen = new AABB(0, 0, Jam.WIDTH_SCALED, Jam.HEIGHT_SCALED);
    private final AABB largeScreen = new AABB(-100, -100, Jam.WIDTH_SCALED + 200, Jam.HEIGHT_SCALED + 200);

    public Game() {
        player = new Player(this, Jam.WIDTH_SCALED / 2, Jam.HEIGHT_SCALED / 2);
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemiesBullets = new ArrayList<>();

        Texture t = TextureCache.get("textures/tileset.png");
        ITexture texture = new SubTexture(32, 96, 10, 3, t);
        myBullet = new BasicBullet(this, true, (tick) -> {
            float v = tick * GameEngine.INV_TPS;

            float vx = Math.cos(v);
            float vy = Math.sin(v);

            return new Vector2f(vx, vy);
        }, new Vector2f(player.getPos()), texture);
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
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
                    0, f2d.getFont().getHeight());
            f2d.drawString("Number of bullets: %d".formatted(playerBullets.size()),
                    0, f2d.getFont().getHeight() * 2);
        }
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
