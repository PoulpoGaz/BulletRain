package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.Enemy;
import fr.poulpogaz.jam.entities.Player;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
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

import static fr.poulpogaz.jam.Constants.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class Game extends State {

    private static final Logger LOGGER = LogManager.getLogger(Game.class);

    // part of the map that is visible
    private final AABB screen = new AABB(0, 0, WIDTH, HEIGHT);

    // part of the map that is either visible or need to be updated
    private final AABB largeScreen = new AABB(-OUTER_SCREEN_SIZE,
            -OUTER_SCREEN_SIZE,
            WIDTH + OUTER_SCREEN_SIZE * 2,
            HEIGHT + OUTER_SCREEN_SIZE * 2);

    private Stage stage = Stages.LEVEL_1;
    private int nextEnemyToAdd = 0;
    private Texture background;
    private float mapScroll;

    // entities that are on the screen
    private Player player;
    private final List<Enemy> enemies;
    private final List<Bullet> playerBullets;
    private final List<Bullet> enemiesBullets;

    public Game() {
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemiesBullets = new ArrayList<>();
    }

    @Override
    public void show() throws Exception {
        stage.loadAllTextures();

        background = TextureCache.get(stage.getBackground());

        mapScroll = -50;
        player = new Player(this, new Vector2f(HALF_WIDTH, Q3_HEIGHT));
        player.getRenderer().loadTextures();
    }

    @Override
    protected void renderBackground(Graphics2D g2d, FontRenderer f2d) {
        float texY = background.getHeight() - Mathf.absMod(mapScroll, background.getHeight()) - HEIGHT;

        if (texY < 0) {
            float texY2 = background.getHeight() + texY;
            g2d.drawSprite(background, 0, 0, WIDTH, -texY, 0, texY2, background.getWidth(), -texY);
            g2d.drawSprite(background, 0, -texY, WIDTH, HEIGHT + texY, 0, 0, background.getWidth(), HEIGHT + texY);
        } else {
            g2d.drawSprite(background, 0, 0, WIDTH, HEIGHT, 0, texY, background.getWidth(), HEIGHT);
        }
    }

    @Override
    protected void renderForeground(Graphics2D g2d, FontRenderer f2d) {
        GLUtils.blend();

        drawEntities(g2d, f2d);

        if (DEBUG) {
            drawDebugInfo(g2d, f2d);
        }

        f2d.flush();
        g2d.end();
        GLUtils.noBlend();
    }

    private void drawEntities(Graphics2D g2d, FontRenderer f2d) {
        for (Enemy e : enemies) {
            AABB aabb = e.getAABB();

            if (aabb.collide(screen).intersect()) {
                e.render(g2d, f2d);
            }
        }

        drawBullets(playerBullets, g2d, f2d);

        if (player.getAABB().collide(screen).intersect()) {
            player.render(g2d, f2d);
        }

        drawBullets(enemiesBullets, g2d, f2d);
    }

    private void drawBullets(List<Bullet> bullets, Graphics2D g2d, FontRenderer f2d) {
        for (Bullet p : bullets) {
            AABB aabb = p.getAABB();

            if (aabb.collide(screen).intersect()) {
                p.render(g2d, f2d);
            }
        }
    }

    private void drawDebugInfo(Graphics2D g2d, FontRenderer f2d) {
        int h = f2d.getFont().getHeight();
        f2d.drawString("Player: (%.0f, %.0f)".formatted(player.getX(), player.getY()),
                0, 0);
        f2d.drawString("Map scroll: %.0f".formatted(mapScroll), 0, h);
        f2d.drawString("Number of player bullets: %d".formatted(playerBullets.size()),
                0, h * 2);
        f2d.drawString("Number of enemies bullets: %d".formatted(enemiesBullets.size()),
                0, h * 3);
        f2d.drawString("Number of enemies: %d".formatted(enemies.size()),
                0, h * 4);
    }

    @Override
    public void update(float delta) {
        for (Enemy e : enemies) {
            e.update(input, delta);
        }

        player.update(input, delta);
        updateBullets(playerBullets, delta);
        updateBullets(enemiesBullets, delta);
        spawnEnemies();

        if (input.keyPressed(GLFW_KEY_R)) {
            mapScroll = -100;
            nextEnemyToAdd = 0;
            enemies.clear();
            playerBullets.clear();
            enemiesBullets.clear();
        }

        mapScroll += MAP_SCROLL_SPEED;
    }

    private void updateBullets(List<Bullet> bullets, float delta) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet p = bullets.get(i);

            if (largeScreen.collide(p.getAABB()).intersect()) {
                p.update(input, delta);
            } else {
                // remove, bullet is out of the screen and
                // there is practically no chance the bullet will one day come back
                bullets.remove(i);
                i--;
            }
        }
    }

    private void spawnEnemies() {
        List<EnemyScript> scripts = stage.getScripts();

        EnemyScript s;
        while (nextEnemyToAdd < scripts.size()) {
            s = scripts.get(nextEnemyToAdd);

            if (s.triggerTime() <= mapScroll) {
                Enemy e = new Enemy(this, s);
                LOGGER.debug("New enemy: {}. t={}, loc={}", s.enemy().name(), mapScroll, e.getPos());

                enemies.add(e);
                nextEnemyToAdd++;
            } else {
                break;
            }
        }
    }

    @Override
    public void dispose() {

    }

    public void addBullet(Bullet bullet) {
        if (bullet.isPlayerBullet()) {
            playerBullets.add(bullet);
        } else {
            enemiesBullets.add(bullet);
        }
    }

    public float getMapScroll() {
        return mapScroll;
    }

    public Player getPlayer() {
        return player;
    }

    public Stage getStage() {
        return stage;
    }
}
