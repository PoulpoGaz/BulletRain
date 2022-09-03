package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.poulpogaz.jam.engine.AABB;
import fr.poulpogaz.jam.engine.Circle;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.engine.Polygon;
import fr.poulpogaz.jam.entities.*;
import fr.poulpogaz.jam.particles.AnimatedParticle;
import fr.poulpogaz.jam.particles.Particle;
import fr.poulpogaz.jam.stage.EnemyScript;
import fr.poulpogaz.jam.stage.Sequence;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.stage.Stages;
import fr.poulpogaz.jam.utils.Animations;
import fr.poulpogaz.jam.utils.Mathf;
import fr.poulpogaz.jam.utils.Size;
import fr.poulpogaz.jam.utils.Utils;

import java.math.BigDecimal;
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
    private final AABB screen = new AABB(0, 0,  MAP_WIDTH, MAP_HEIGHT);

    // part of the map that is either visible or need to be updated
    private final AABB largeScreen = new AABB(-OUTER_SCREEN_SIZE,
            -OUTER_SCREEN_SIZE,
            MAP_WIDTH + OUTER_SCREEN_SIZE * 2,
            MAP_HEIGHT + OUTER_SCREEN_SIZE * 2);

    private Stage stage = Stages.LEVEL_1;
    private int currentSeqIndex = 0;
    private int nextEnemyToAdd = 0;
    private int waitSpawn = 0;
    private int sequenceStart = 0;

    private Texture background;
    private float mapScroll;
    private int tick;

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
        ScreenUtils.clear(0, 0, 0, 1);

        spriteBatch.begin();
        drawBackground();
        drawForeground();
        drawInformationPanel();
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
        shapeRenderer.rect(0, 0, MAP_WIDTH, MAP_HEIGHT);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    protected void drawPauseMenuAndUpdate() {
        drawGrayMask(0.5f);

        spriteBatch.enableBlending();
        spriteBatch.begin();
        pauseMenu.getPreferredSize(font, size);
        pauseMenu.draw(spriteBatch, font, M_Q_WIDTH, M_Q3_HEIGHT, size);
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
        deadMenu.draw(spriteBatch, font, M_Q_WIDTH, M_Q3_HEIGHT, size);
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





    protected void drawBackground() {
        float texY = Mathf.absMod(mapScroll, background.getHeight());

        if (texY + MAP_HEIGHT >= background.getHeight()) {
            float u = 0;
            float v = texY / background.getHeight();
            float u2 = 1;
            float v2 = 1;

            float firstPartH = background.getHeight() - texY;
            spriteBatch.draw(background, 0, 0, MAP_WIDTH, firstPartH, u, v, u2, v2);

            float secondPartH = MAP_HEIGHT - firstPartH;
            u = 0;
            v = 0;
            u2 = 1;
            v2 = secondPartH / background.getHeight();

            spriteBatch.draw(background, 0, firstPartH, MAP_WIDTH, secondPartH, u, v, u2, v2);
        } else {
            float u = 0;
            float v = texY / background.getHeight();
            float u2 = 1;
            float v2 = (texY + MAP_HEIGHT) / background.getHeight();

            spriteBatch.draw(background, 0, 0, MAP_WIDTH, MAP_HEIGHT, u, v, u2, v2);
        }
    }

    protected void drawForeground() {
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
            player.draw(spriteBatch, font);
        }

        drawEntities(enemiesBullets);
        drawEntities(items);
    }

    private <T extends Entity> void drawEntities(Array<T> entities) {
        for (T p : entities) {
            AABB aabb = p.getAABB();

            if (aabb.collide(screen)) {
                p.draw(spriteBatch, font);
            }
        }
    }

    private void drawHitBox(Entity entity) {
        AABB a = entity.getAABB();

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(a.getX(), a.getY(), a.getWidth(), a.getHeight());

        HitBox p = entity.getDetailedHitBox();

        shapeRenderer.setColor(0, 1, 0, 1);
        if (p instanceof Polygon) {
            Polygon poly = (Polygon) p;

            List<Vector2> points = poly.getPoints();
            for (int i = 0; i < points.size(); i++) {
                Vector2 p1 = points.get(i);
                Vector2 p2 = points.get((i + 1) % points.size());

                shapeRenderer.line(p1, p2);
            }
        } else if (p instanceof Circle) {
            Circle c = (Circle) p;

            shapeRenderer.circle(c.getCenter().x, c.getCenter().y, c.getRadius(), 8);
        }

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 0);
        shapeRenderer.circle(entity.getX(), entity.getY(), 3, 6);

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }


    private void drawInformationPanel() {
        spriteBatch.enableBlending();

        BitmapFont font = jam.getFont42();
        float scaleX = font.getScaleX();
        float scaleY = font.getScaleY();

        font.getData().setScale(3 / 8f);

        float y = HEIGHT - 50;
        font.draw(spriteBatch, "Level 1", MAP_WIDTH, y, WIDTH - MAP_WIDTH, Align.center, false);

        y -= 2.5f * font.getLineHeight();
        font.draw(spriteBatch, "Power", MAP_WIDTH + 30, y);
        float x = drawPower(font, MAP_WIDTH + 100, y, player.getPower()) + 5;
        x += font.draw(spriteBatch, "/", x, y).width + 5;
        drawPower(font, x, y, 4);

        y -= 1.5f * font.getLineHeight();
        font.draw(spriteBatch, "Score", MAP_WIDTH + 30, y);

        String score = Utils.toString(player.getScore(), 9);
        font.draw(spriteBatch, score, MAP_WIDTH + 100, y);

        font.getData().setScale(scaleX, scaleY);

        spriteBatch.disableBlending();
    }

    private float drawPower(BitmapFont font, float x, float y, double power) {
        BigDecimal dec = BigDecimal.valueOf(power);

        int first = dec.intValue();
        int second = dec.scaleByPowerOfTen(1).intValue() - 10 * first;
        int third = dec.scaleByPowerOfTen(2).intValue() - 100 * first - 10 * second;

        if (third > 0) {
            third = 5;
        }

        x += font.draw(spriteBatch, first + ".", x, y).width;

        float sx = font.getScaleX();
        float sy = font.getScaleY();

        x += font.draw(spriteBatch, Utils.toString(second * 10 + third, 2), x, y - 3).width;

        font.getData().setScale(sx, sy);

        return x;
    }

    // **********
    // * UPDATE *
    // **********


    private void update(float delta) {
        for (int i = 0; i < enemies.size; i++) {
            Enemy e = enemies.get(i);

            e.update(delta);

            if ((e.isDead() && !e.hasParticles()) || !largeScreen.collide(e.getAABB())) {
                enemies.removeIndex(i);
                e.kill();
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

        if (player.getPower() >= PLAYER_MAX_POWER) {
            for (int i = 0; i < items.size; i++) {
                Item item = items.get(i);

                if (item.getType() == Item.POWER) {
                    particles.add(new AnimatedParticle(new Vector2(item.getPos()), Animations.get("hit")));
                    items.removeIndex(i);
                    ITEM_POOL.free(item);
                }
            }
        }

        if (enemies.isEmpty() && nextEnemyToAdd >= stage.getSequences().size()) {
            // jam.setScreen(jam.getWinScreen());
        }

        mapScroll += MAP_SCROLL_SPEED;

        if (tick >= 0) {
            tick++;

            if (waitSpawn > 0) {
                waitSpawn--;
            }

            if (waitSpawn < 0 && enemiesBullets.isEmpty()) {
                waitSpawn = 0;
            }
        }

        // "real" start of the game
        if (mapScroll >= 0 && tick < 0) {
            tick = 0;
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
        // increase score
        player.killEnemy();

        // add items
        int l = e.getMaxLife();

        double n = Math.ceil(Math.log(l) / Mathf.LN_2);
        int numItem = (int) (Math.max(n, 1) * e.getDescriptor().dropRate());
        int numScore = 0;
        int numPower = 0;

        if (numItem == 0) {
            return;
        }

        if (player.getPower() >= PLAYER_MAX_POWER) {
            numScore = numItem;
        } else if (numItem == 1) {
            numPower = 1;
        } else {
            numPower = 1 + Mathf.RANDOM.nextInt(numItem - 1);
            numScore = numItem - numPower;
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

            item.set(x, y, Item.POWER, POWER_BLOCK_VALUE);

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
        if (tick < 0 || waitSpawn != 0 || currentSeqIndex >= stage.getSequences().size()) {
            return;
        }

        Sequence current = stage.getSequences().get(currentSeqIndex);

        if (nextEnemyToAdd >= current.size() && enemies.isEmpty()) {
            waitSpawn = current.waitAfterEnd();
            currentSeqIndex++;
            nextEnemyToAdd = 0;
            sequenceStart = tick + waitSpawn;
            return;
        }

        int t = tick - sequenceStart;
        EnemyScript s;
        while (nextEnemyToAdd < current.size()) {
            s = current.get(nextEnemyToAdd);

            if (s.triggerTime() <= t) {
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
        currentSeqIndex = 0;
        waitSpawn = 0;
        sequenceStart = 0;
        tick = -1;
    }


    @Override
    public void resize(int MAP_WIDTH, int MAP_HEIGHT) {

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
        out.add("Tick: " + tick);
        out.add("Number of player bullets: " + playerBullets.size);
        out.add("Number of enemies bullets: " + enemiesBullets.size);
        out.add("Number of enemies: " + enemies.size);
        out.add("Number of particles: " + particles.size);
    }

    public Enemy nearestEnemy(Bullet b) {
        Enemy min = null;
        float minDist = Float.MAX_VALUE;

        for (Enemy e : enemies) {
            if (e.isDead()) {
                continue;
            }

            float d = e.getPos().dst2(b.getPos());

            if (d < minDist) {
                min = e;
                minDist = d;
            }
        }

        return min;
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
