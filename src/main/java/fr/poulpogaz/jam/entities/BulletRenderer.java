package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.states.Game;

public class BulletRenderer extends TextureEntityRenderer {

    public BulletRenderer(String texture) {
        super(texture);
    }

    public BulletRenderer(String texture, int x, int y, int w, int h) {
        super(texture, x, y, w, h);
    }


    @Override
    public void render(Graphics2D g2d, FontRenderer f2d, Game game, Entity entity) {
        if (entity instanceof Bullet bullet) {
            g2d.translate(bullet.getX(), entity.getY());
            g2d.rotate(bullet.getAngle());
            g2d.drawSprite(tex, -tex.getWidth() / 2f, -tex.getHeight() / 2f);
            g2d.rotate(-bullet.getAngle());
            g2d.translate(-bullet.getX(), -bullet.getY());
        }
    }
}
