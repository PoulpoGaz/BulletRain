package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.engine.Circle;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.PlayerBulletPattern;

import static fr.poulpogaz.jam.Constants.*;

public class Player extends LivingEntity {

    private final Circle hitBox;
    private double power;

    private final BulletPattern pattern = new PlayerBulletPattern(this);

    private boolean slowdown;
    private boolean lastWasShooting = false;

    private float attractionPower = Constants.PLAYER_MIN_ATTRACTION;

    private int tickSinceLeftRightOnBorder = 0;

    public Player(GameScreen game) {
        super(game);
        life = 1;
        maxLife = 1;
        this.pos = new Vector2(M_HALF_WIDTH, M_Q_HEIGHT);

        power = Constants.PLAYER_MIN_POWER;
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

            if (getPos().y >= M_Q3_HEIGHT) {
                attractionPower = Float.MAX_VALUE;
            } else {
                attractionPower = PLAYER_MIN_ATTRACTION;
            }

            if (moved) {
                markDirty();
            }

            game.getPlayerScore().survive();
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

            if (pos.x == 0) {
                tickSinceLeftRightOnBorder++;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (slowdown) {
                vx += Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx += Constants.PLAYER_SPEED;
            }

            if (pos.x == MAP_WIDTH) {
                tickSinceLeftRightOnBorder++;
            }
        }

        if (tickSinceLeftRightOnBorder >= 30) {
            if (pos.x == 0) {
                pos.x = MAP_WIDTH;
            } else {
                pos.x = 0;
            }

            tickSinceLeftRightOnBorder = 0;

            return true;
        } else {
            pos.add(vx, vy);

            pos.x = MathUtils.clamp(pos.x, 0, Constants.MAP_WIDTH);
            pos.y = MathUtils.clamp(pos.y, 0, Constants.MAP_HEIGHT);

            return vx != 0 || vy != 0;
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        hitBox.getCenter().set(pos);
        return hitBox;
    }

    public double getPower() {
        return power;
    }

    public boolean isSlowdown() {
        return slowdown;
    }

    public float getAttractionPower() {
        return attractionPower;
    }

    public void pick(Item item) {
        item.setPicked(true);

        if (item.getType() == Item.POWER) {
            power += item.getValue();

            if (power > PLAYER_MAX_POWER) {
                power = PLAYER_MAX_POWER;
            }
        } else if (item.getType() == Item.SCORE) {
            game.getPlayerScore().addItem();
        }
    }

    public void reset() {
        pos.set(M_HALF_WIDTH, M_Q_HEIGHT);
        particles.clear();
        hit = false;
        slowdown = false;
        lastWasShooting = false;
        life = 1;
        power = Constants.PLAYER_MIN_POWER;
        death = null;
        attractionPower = Constants.PLAYER_MIN_ATTRACTION;
        markDirty();
    }

    public void resetForContinue() {
        pos.set(M_HALF_WIDTH, M_Q_HEIGHT);
        particles.clear();
        hit = false;
        slowdown = false;
        lastWasShooting = false;
        life = 1;
        death = null;
        attractionPower = Constants.PLAYER_MIN_ATTRACTION;
        markDirty();
    }

    public void setPower(double power) {
        this.power = power;
    }

    private class PlayerEntityRenderer extends TextureEntityRenderer {

        public PlayerEntityRenderer() {
            super("tileset.png", 0, 96, 27, 35);
        }

        @Override
        public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
            super.render(batch, font, game, entity);

            if (isSlowdown()) {
                batch.end();

                ShapeRenderer sr = Jam.INSTANCE.getShape();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(1, 0, 0, 1);
                sr.circle(pos.x, pos.y, hitBox.getRadius(), 6);
                sr.end();

                batch.begin();

            }
        }
    }
}
