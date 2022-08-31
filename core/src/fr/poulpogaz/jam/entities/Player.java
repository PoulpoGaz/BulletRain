package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.Circle;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.PlayerBulletPattern;

import static fr.poulpogaz.jam.Constants.HALF_WIDTH;
import static fr.poulpogaz.jam.Constants.Q_HEIGHT;

public class Player extends LivingEntity {

    private final Circle hitBox;
    private float power;

    private final BulletPattern pattern = new PlayerBulletPattern(this);

    private boolean slowdown;
    private boolean lastWasShooting = false;

    public Player(GameScreen game) {
        super(game);
        life = 1;
        this.pos = new Vector2(HALF_WIDTH, Q_HEIGHT);

        power = 1;
        hitBox = new Circle(pos, Constants.PLAYER_HITBOX_RAD);
    }

    @Override
    protected EntityRenderer createRenderer() {
        return new PlayerEntityRenderer();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isAlive()) {
            slowdown = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
            boolean moved = move();

            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                if (!lastWasShooting) {
                    pattern.reset();
                }

                pattern.addBullets(game, this, true);

                lastWasShooting = true;
            } else {
                lastWasShooting = false;
            }

            if (moved) {
                markDirty();
            }
        }
    }

    private boolean move() {
        float vx = 0;
        float vy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (slowdown) {
                vy -= Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy -= Constants.PLAYER_SPEED;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (slowdown) {
                vy += Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy += Constants.PLAYER_SPEED;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (slowdown) {
                vx -= Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx -= Constants.PLAYER_SPEED;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (slowdown) {
                vx += Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx += Constants.PLAYER_SPEED;
            }
        }

        pos.add(vx, vy);

        pos.x = MathUtils.clamp(pos.x, 0, Constants.WIDTH);
        pos.y = MathUtils.clamp(pos.y, 0, Constants.HEIGHT);

        return vx != 0 || vy != 0;
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        hitBox.getCenter().set(pos);
        return hitBox;
    }

    public float getPower() {
        return power;
    }

    public boolean isSlowdown() {
        return slowdown;
    }

    public void reset() {
        pos.set(HALF_WIDTH, Q_HEIGHT);
        particles.clear();
        hit = false;
        slowdown = false;
        lastWasShooting = false;
        life = 1;
        power = 1f;
        death = null;
        markDirty();
    }

    private class PlayerEntityRenderer extends TextureEntityRenderer {

        public PlayerEntityRenderer() {
            super("tileset.png", 0, 96, 27, 35);
        }

        @Override
        public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
            super.render(batch, font, game, entity);

            if (isSlowdown()) {
                // g2d.setColor(Colors.RED);
                // g2d.fillCircle(pos.x, pos.y, hitBox.getRadius(), 6);
            }
        }
    }
}
