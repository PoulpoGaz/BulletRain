package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.entity.AbstractBullet;
import fr.poulpogaz.jam.entity.Enemy;
import fr.poulpogaz.jam.entity.Player;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.stage.EnemyDescriptor;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.stage.Stages;
import fr.poulpogaz.jam.utils.GLUtils;
import fr.poulpogaz.jam.utils.Mathf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Game extends State {

    private static final Logger LOGGER = LogManager.getLogger(Game.class);

    private final AABB screen = new AABB(0, 0, Jam.WIDTH, Jam.HEIGHT);
    private final AABB largeScreen = new AABB(-50, -50, Jam.WIDTH + 100, Jam.HEIGHT + 100);

    private Stage stage = Stages.LEVEL_1;
    private int nextEnemyToAdd = 0;
    private Texture background;
    private int mapY;

    // entities that are on the screen
    private Player player;
    private final List<Enemy> enemies;
    private final List<AbstractBullet> playerBullets;
    private final List<AbstractBullet> enemiesBullets;

    public Game() {
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemiesBullets = new ArrayList<>();
    }

    @Override
    public void show() throws Exception {
        background = TextureCache.getOrCreate(stage.getBackground());

        for (EnemyDescriptor desc : stage.getDescriptors()) {
            desc.renderer().loadTextures();
        }

        player = new Player(this, Jam.WIDTH / 2, Jam.HEIGHT / 2);

        mapY = (int) (-Jam.WIDTH * 1.2);
    }

    @Override
    protected void renderBackground(Graphics2D g2d, FontRenderer f2d) {
        int texY = background.getHeight() - Mathf.absMod(mapY, background.getHeight()) - Jam.HEIGHT;

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

        for (Enemy e : enemies) {
            AABB aabb = e.aabb();

            if (aabb == null || aabb.collide(screen).intersect()) {
                e.render(g2d, f2d);
            }
        }

        player.render(g2d, f2d);

        for (AbstractBullet p : playerBullets) {
            AABB aabb = p.aabb();

            if (aabb.collide(screen).intersect()) {
                p.render(g2d, f2d);
            }
        }

        if (Constants.DEBUG) {
            int h = f2d.getFont().getHeight();
            f2d.drawString("Player: (%d, %d)".formatted((int) player.getX(), (int) player.getY()),
                    0, 0);
            f2d.drawString("Map Y: %d".formatted(mapY), 0, h);
            f2d.drawString("Number of bullets: %d".formatted(playerBullets.size()),
                    0, h * 2);
        }
        GLUtils.noBlend();
    }

    @Override
    public void update(float delta) {
        for (Enemy e : enemies) {
            e.update(input, delta);
        }

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

        addEnemies();

        mapY++;
    }

    private void addEnemies() {
        List<EnemyScript> scripts = stage.getScripts();

        EnemyScript s;
        while (nextEnemyToAdd < scripts.size()) {
            s = scripts.get(nextEnemyToAdd);

            if (s.startPos().y <= mapY) {
                enemies.add(s.createEnemy(this));
                nextEnemyToAdd++;
            } else {
                break;
            }
        }
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
