package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.engine.polygons.Circle;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.PlayerBulletPattern;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Math;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends LivingEntity {

    private final Circle hitBox;
    private float power;

    private BulletPattern pattern = new PlayerBulletPattern(this);

    private boolean slowdown;

    private boolean lastWasShooting = false;

    public Player(Game game, Vector2f pos) {
        super(game);
        life = 1;
        this.pos = new Vector2f(pos);

        power = 4f;
        hitBox = new Circle(Constants.PLAYER_HITBOX_RAD, pos); // same instance of pos!!
    }

    @Override
    protected EntityRenderer createRenderer() {
        return new PlayerEntityRenderer();
    }

    @Override
    public void update(Input in, float delta) {
        if (isDied()) {

        } else {
            slowdown = in.keyPressed(GLFW_KEY_LEFT_SHIFT) || in.keyPressed(GLFW_KEY_RIGHT_SHIFT);
            move(in);

            if (in.keyPressed(GLFW_KEY_X)) {
                if (!lastWasShooting) {
                    pattern.reset();
                }

                pattern.addBullets(game, this, true);

                lastWasShooting = true;
            } else {
                lastWasShooting = false;
            }
        }

        clean();
    }

    private void move(Input in) {
        float vx = 0;
        float vy = 0;
        if (in.keyPressed(GLFW_KEY_DOWN)) {
            if (slowdown) {
                vy += Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy += Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_UP)) {
            if (slowdown) {
                vy -= Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy -= Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_LEFT)) {
            if (slowdown) {
                vx -= Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx -= Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_RIGHT)) {
            if (slowdown) {
                vx += Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx += Constants.PLAYER_SPEED;
            }
        }

        pos.add(vx, vy);

        pos.x = Math.clamp(0, Constants.WIDTH, pos.x);
        pos.y = Math.clamp(0, Constants.HEIGHT, pos.y);
    }

    @Override
    protected Polygon getDetailedHitBoxImpl() {
        hitBox.setCenter(pos);
        return hitBox;
    }

    public float getPower() {
        return power;
    }

    public boolean isSlowdown() {
        return slowdown;
    }

    private class PlayerEntityRenderer extends TextureEntityRenderer {

        public PlayerEntityRenderer() {
            super("tileset.png", 0, 96, 27, 35);
        }

        @Override
        public void render(Graphics2D g2d, FontRenderer f2d, Game game, Entity entity) {
            super.render(g2d, f2d, game, entity);

            if (isSlowdown()) {
                g2d.setColor(Colors.RED);
                g2d.fillCircle(pos.x, pos.y, hitBox.getRadius(), 6);
            }
        }
    }
}
