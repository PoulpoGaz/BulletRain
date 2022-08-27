package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.renderer.utils.TextureCache;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {

    private ITexture texture;

    public Player() {
        Texture tex = TextureCache.get("textures/tileset.png");
        texture = new SubTexture(0, 96, 27, 35, tex);

        x = Jam.WIDTH_SCALED / 2;
        y = Jam.HEIGHT_SCALED / 2;
    }

    @Override
    public void update(Input in, float delta) {
        boolean slowDown = in.keyPressed(GLFW_KEY_LEFT_SHIFT) || in.keyPressed(GLFW_KEY_RIGHT_SHIFT);

        // oof
        int vx = 0;
        int vy = 0;
        if (in.keyPressed(GLFW_KEY_DOWN)) {
            if (slowDown) {
                vy = -Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy = -Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_UP)) {
            if (slowDown) {
                vy = Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vy = Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_LEFT)) {
            if (slowDown) {
                vx = -Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx = -Constants.PLAYER_SPEED;
            }
        }

        if (in.keyPressed(GLFW_KEY_RIGHT)) {
            if (slowDown) {
                vx = Constants.PLAYER_SPEED_SLOW_DOWN;
            } else {
                vx = Constants.PLAYER_SPEED;
            }
        }

        x += vx;
        y += vy;
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        g2d.drawSprite(texture, x - texture.getWidth() / 2f, y - texture.getHeight() / 2f);
    }
}
