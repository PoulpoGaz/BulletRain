package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import fr.poulpogaz.jam.engine.AABB;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.entities.*;
import fr.poulpogaz.jam.particles.Particle;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.stage.Stages;
import fr.poulpogaz.jam.utils.Animations;
import fr.poulpogaz.jam.utils.Mathf;
import fr.poulpogaz.jam.utils.Size;
import fr.poulpogaz.jam.utils.Utils;

import java.util.List;

import static fr.poulpogaz.jam.Constants.*;

public class GameScreen extends AbstractScreen {

    // pools
    private final Pool<Item> ITEM_POOL = new Pool<Item>() {

        @Override
        protected Item newObject() {
            return new Item(GameScreen.this);
        }
    };



    // part of the map that is visible
    private final AABB screen = new AABB(0, 0,  WIDTH, HEIGHT);

    // part of the map that is either visible or need to be updated
    private final AABB largeScreen = new AABB(-OUTER_SCREEN_SIZE,
            -OUTER_SCREEN_SIZE,
            WIDTH + OUTER_SCREEN_SIZE * 2,
            HEIGHT + OUTER_SCREEN_SIZE * 2);

    private Stage stage = Stages.LEVEL_1;
    private int nextEnemyToAdd = 0;
    private Texture background;
    private float mapScroll;
    private float timeSinceStart;

    // entities that are on the screen
    private Player player;
    private final Array<Enemy> enemies;
    private final Array<Bullet> playerBullets;
    private final Array<Bullet> enemiesBullets;
    private final Array<Item> items;

    // particles
    private final Array<Particle> particles;

    // menu
    private final Size size = new Size();
    private final Menu pauseMenu;
    private final Menu deadMenu;

    public GameScreen(Jam jam) {
        super(jam);
        enemies = new Array<>();
        playerBullets = new Array<>();
        enemiesBullets = new Array<>();
        particles = new Array<>();
        items = new Array<>();

        pauseMenu = new Menu();
        pauseMenu.addLabel("Continue");
        pauseMenu.addLabel("Restart");
        pauseMenu.addLabel("Exit");
        pauseMenu.setNotSelectedColor(new Color(0.7f, 0.7f, 0.7f, 1));

        deadMenu = new Menu();
        deadMenu.addLabel("Restart");
        deadMenu.addLabel("Exit");
        deadMenu.setNotSelectedColor(new Color(0.7f, 0.7f, 0.7f, 1));
    }

    @Override
    public void preLoad() {
        stage.loadAllTextures();
        player = new Player(this);
        player.getRenderer().loadTextures();

        Jam.getOrLoadTexture("explosions.png");
        Item.renderer.loadTextures();
    }

    @Override
    public void show() {
        mapScroll = -50;
        background = Jam.getTexture(stage.getBackground());
        Animations.loadAnimations();

        pauseMenu.setVisible(false);
        deadMenu.setVisible(false);

        restart();
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        renderBackground();
        renderForeground();
        spriteBatch.end();

        if (pauseMenu.isVisible()) {
            drawPauseMenuAndUpdate();
        } else if (deadMenu.isVisible()) {
            drawDeadMenuAndUpdate();
        } else {
            update(delta);

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                if (player.isDying()) {
                    deadMenu.setVisible(true);
                } else {
                    pauseMenu.setVisible(true);
                }
            }
        }
    }

    protected void drawGrayMask(float alpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, alpha);
        shapeRenderer.rect(0, 0, WIDTH, HEIGHT);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    protected void drawPauseMenuAndUpdate() {
        drawGrayMask(0.5f);

        spriteBatch.enableBlending();
        spriteBatch.begin();
        pauseMenu.getPreferredSize(font, size);
        pauseMenu.draw(spriteBatch, font, Q_WIDTH, Q3_HEIGHT, size);
        spriteBatch.end();
        spriteBatch.disableBlending();

        int s = pauseMenu.update();

        if (s == 0) {
            pauseMenu.setVisible(false);
        } else if (s == 1) {
            restart();
            pauseMenu.setVisible(false);
        } else if (s == 2) {
            jam.setScreen(jam.getMainMenuScreen());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseMenu.setVisible(false);
        }
    }


    protected void drawDeadMenuAndUpdate() {
        drawGrayMask(0.5f);

        spriteBatch.enableBlending();
        spriteBatch.begin();
        deadMenu.getPreferredSize(font, size);
        deadMenu.draw(spriteBatch, font, Q_WIDTH, Q3_HEIGHT, size);
        spriteBatch.end();
        spriteBatch.disableBlending();

        int s = deadMenu.update();

        if (s == 0) {
            restart();
            deadMenu.setVisible(false);
        } else if (s == 1) {
            jam.setScreen(jam.getMainMenuScreen());
        }
    }





    protected void renderBackground() {
        float texY = Mathf.absMod(mapScroll, background.getHeight());

        if (texY + HEIGHT >= background.getHeight()) {
            float u = 0;
            float v = texY / background.getHeight();
            float u2 = 1;
            float v2 = 1;

            float firstPartH = background.getHeight() - texY;
            spriteBatch.draw(background, 0, 0, WIDTH, firstPartH, u, v, u2, v2);

            float secondPartH = HEIGHT - firstPartH;
            u = 0;
            v = 0;
            u2 = 1;
            v2 = secondPartH / background.getHeight();

            spriteBatch.draw(background, 0, firstPartH, WIDTH, secondPartH, u, v, u2, v2);
        } else {
            float u = 0;
            float v = texY / background.getHeight();
            float u2 = 1;
            float v2 = (texY + HEIGHT) / background.getHeight();

            spriteBatch.draw(background, 0, 0, WIDTH, HEIGHT, u, v, u2, v2);
        }
    }

    protected void renderForeground() {
        spriteBatch.enableBlending();

        drawEntities();

        for (Particle p : particles) {
            p.render(spriteBatch, font);
        }

        if (player.isDying()) {
            spriteBatch.end();
            drawGrayMask(player.percentToDeath() / 2f);
            spriteBatch.begin();

        } else if (player.isDead() && !player.isDying()) {
            deadMenu.setVisible(true);
        }

        if (Constants.SHOW_HITBOX) {
            spriteBatch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            drawHitBox(player);

            for (Bullet b : playerBullets) drawHitBox(b);
            for (Bullet b : enemiesBullets) drawHitBox(b);
            for (Enemy e : enemies) drawHitBox(e);
            for (Item i : items) drawHitBox(i);

            shapeRenderer.end();
            spriteBatch.begin();
        }

        spriteBatch.disableBlending();
    }

    private void drawEntities() {
        drawEntities(enemies);
        drawEntities(playerBullets);

        if (player.isDying() || !player.isDead()) {
            player.render(spriteBatch, font);
        }

        drawEntities(enemiesBullets);
        drawEntities(items);
    }

    private <T extends Entity> void drawEntities(Array<T> entities) {
        for (T p : entities) {
            AABB aabb = p.getAABB();

            if (aabb.collide(screen)) {
                p.render(spriteBatch, font);
            }
        }
    }

    private void drawHitBox(Entity entity) {
        AABB a = entity.getAABB();

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(a.getX(), a.getY(), a.getWidth(), a.getHeight());
    }




    private void update(float delta) {
        for (int i = 0; i < enemies.size; i++) {
            Enemy e = enemies.get(i);

            e.update(delta);

            if ((e.isDead() && !e.hasParticles()) || !largeScreen.collide(e.getAABB())) {
                enemies.removeIndex(i);
                i--;
            }
        }

        player.update(delta);
        updateBullets(playerBullets, delta);
        updateBullets(enemiesBullets, delta);

        if (!player.isDead()) {
            checkCollisions();
        }

        updateParticles(delta);
        updateItems(delta);
        spawnEnemies();

        if (enemies.isEmpty() && nextEnemyToAdd >= stage.getScripts().size()) {
            jam.setScreen(jam.getWinScreen());
        }

        mapScroll += MAP_SCROLL_SPEED;

        if (timeSinceStart >= 0) {
            timeSinceStart += delta;
        }

        // "real" start of the game
        if (mapScroll >= 0 && timeSinceStart < 0) {
            timeSinceStart = 0;
        }
    }


    private void updateBullets(Array<Bullet> bullets, float delta) {
        int i = 0;
        while (i < bullets.size) {
            Bullet p = bullets.get(i);

            if (largeScreen.collide(p.getAABB())) {
                p.update(delta);
                i++;
            } else {
                // remove, bullet is out of the screen and
                // there is practically no chance the bullet will one day come back
                bullets.removeIndex(i);
            }
        }
    }

    private void checkCollisions() {
        // player bullets vs enemies
        int i = 0;

        loop:
        while (i < playerBullets.size) {
            Bullet playerBullet = playerBullets.get(i);
            AABB bAABB = playerBullet.getAABB();
            HitBox p = playerBullet.getDetailedHitBox();

            for (Enemy e : enemies) {
                if (e.isDead()) {
                    continue;
                }

                AABB eAABB = e.getAABB();
                HitBox pE = e.getDetailedHitBox();

                if (bAABB.collide(eAABB) && p.collide(pE)) {
                    e.hit(playerBullet);

                    if (e.isDead()) {
                        enemyKilled(e);
                    }

                    playerBullets.removeIndex(i);
                    continue loop;
                }
            }

            i++;
        }


        // enemies bullets vs player
        if (!enemiesBullets.isEmpty()) {
            AABB playerAABB = player.getAABB();
            HitBox playerP = player.getDetailedHitBox();

            i = 0;
            while (i < enemiesBullets.size) {
                Bullet bullet = enemiesBullets.get(i);

                AABB eAABB = bullet.getAABB();
                HitBox pE = bullet.getDetailedHitBox();

                if (playerAABB.collide(eAABB) && playerP.collide(pE)) {
                    player.hit(bullet);
                    enemiesBullets.removeIndex(i);
                    break;
                }

                i++;
            }
        }
    }

    private void enemyKilled(Enemy e) {
        int l = e.getMaxLife();

        int numItem = (int) (Math.ceil(Math.log(l) / Mathf.LN_2));
        int numScore;
        int numPower = 0;

        if (player.getPower() >= PLAYER_MAX_POWER) {
            numScore = numItem;
        } else {
            numScore = 2 + Mathf.RANDOM.nextInt(numItem - 2); // the minimum enemy life is 15, thus the minimum numItem is 4
            numPower = numItem - numScore;
        }

        AABB a = e.getAABB();
        for (int i = 0; i < numScore; i++) {
            Item item = ITEM_POOL.obtain();

            float x = a.getX() + Mathf.random(0, a.getWidth());
            float y = a.getY() + Mathf.random(0, a.getHeight());

            item.set(x, y, Item.SCORE, PLAYER_PICK_SCORE_BLOCK);

            items.add(item);
        }

        for (int i = 0; i < numPower; i++) {
            Item item = ITEM_POOL.obtain();

            float x = a.getX() + Mathf.random(0, a.getWidth());
            float y = a.getY() + Mathf.random(0, a.getHeight());

            item.set(x, y, Item.POWER, 0.1f);

            items.add(item);
        }
    }

    private void updateParticles(float delta) {
        int i = 0;
        while (i < particles.size) {
            Particle p = particles.get(i);

            if (p.isDead()) {
                particles.removeIndex(i);
            } else {
                p.update(delta);
                i++;
            }
        }
    }

    private void updateItems(float delta) {
        int i = 0;
        while (i < items.size) {
            Item item = items.get(i);

            if (largeScreen.collide(item.getAABB())) {
                item.update(delta);

                if (item.isPicked()) {
                    ITEM_POOL.free(item);
                    items.removeIndex(i);
                } else {
                    i++;
                }
            } else {
                ITEM_POOL.free(item);
                items.removeIndex(i);
            }
        }
    }

    private void spawnEnemies() {
        List<EnemyScript> scripts = stage.getScripts();

        EnemyScript s;
        while (nextEnemyToAdd < scripts.size()) {
            s = scripts.get(nextEnemyToAdd);

            if (s.triggerTime() <= timeSinceStart) {
                Enemy e = new Enemy(this, s);

                enemies.add(e);
                nextEnemyToAdd++;
            } else {
                break;
            }
        }
    }




    private void restart() {
        player.reset();
        mapScroll = -50;

        enemiesBullets.clear();
        playerBullets.clear();
        enemies.clear();
        items.clear();
        nextEnemyToAdd = 0;
        timeSinceStart = -1;
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        pauseMenu.setVisible(true);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void getDebugInfo(List<String> out) {
        out.add("Player: (" + (int) player.getX() + ", " + (int) + player.getY() + ")");
        out.add("Map scroll: " + (int) mapScroll);
        out.add("Number of player bullets: " + playerBullets.size);
        out.add("Number of enemies bullets: " + enemiesBullets.size);
        out.add("Number of enemies: " + enemies.size);
        out.add("Number of particles: " + particles.size);
        out.add("Time since start: " + Utils.round2(timeSinceStart));
        out.add("Power: " + player.getPower());
        out.add("Score: " + player.getScore());
    }


    public void addBullet(Bullet bullet) {
        if (bullet.isPlayerBullet()) {
            playerBullets.add(bullet);
        } else {
            enemiesBullets.add(bullet);
        }
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
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
