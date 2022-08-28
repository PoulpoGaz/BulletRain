package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Circle;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.PlayerBulletPattern;
import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.utils.GLUtils;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class Player extends Entity {

    private final ITexture texture;
    private Circle hitBox;
    private float power;

    private BulletPattern pattern = new PlayerBulletPattern(this);

    private boolean slowdown;

    private boolean lastWasShooting = false;

    public Player(Game game, int x, int y) {
        super(game);
        pos = new Vector2f(x, y);

        Texture tex = TextureCache.get("tileset.png");
        texture = new SubTexture(0, 96, 27, 35, tex);

        power = 4f;
        hitBox = new Circle(4, pos); // same instance of pos!!
    }

    @Override
    public void update(Input in, float delta) {
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

    private void move(Input in) {
        int vx = 0;
        int vy = 0;
        if (in.keyPressed(GLFW_KEY_DOWN)) {
            if (slowdown) {
                vy = Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy = Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_UP)) {
            if (slowdown) {
                vy = -Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy = -Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_LEFT)) {
            if (slowdown) {
                vx = -Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx = -Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_RIGHT)) {
            if (slowdown) {
                vx = Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx = Constants.PLAYER_SPEED;
            }
        }

        pos.add(vx, vy);

        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x >= Jam.WIDTH) {
            pos.x = Jam.WIDTH - 1;
        }

        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y >= Jam.HEIGHT) {
            pos.y = Jam.HEIGHT - 1;
        }
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        g2d.drawSprite(texture, pos.x - texture.getWidth() / 2f, pos.y - texture.getHeight() / 2f);

        if (slowdown) {
            g2d.setColor(Colors.RED);
            g2d.fillCircle(pos.x, pos.y, hitBox.getRadius(), 6);
        }
    }

    @Override
    public Polygon getDetailedHitBox() {
        return hitBox;
    }

    @Override
    public AABB aabb() {
        return hitBox.getAABB();
    }

    public float getPower() {
        return power;
    }
}
