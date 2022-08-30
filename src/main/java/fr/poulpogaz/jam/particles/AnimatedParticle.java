package fr.poulpogaz.jam.particles;

import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.utils.Animation;
import fr.poulpogaz.jam.utils.AnimationDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.io.IOException;

public class AnimatedParticle extends Particle {

    private static final Logger LOGGER = LogManager.getLogger(AnimatedParticle.class);

    private final Animation animation;

    public AnimatedParticle(Vector2f pos, AnimationDescriptor desc) {
        super(desc.getDuration());
        this.pos = pos;
        this.animation = desc.createAnimation();

        try {
            animation.loadTextures();
        } catch (IOException e) {
            LOGGER.warn("Failed to load textures for animation {}", desc.getName());
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animation.update();
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        ITexture t = animation.getCurrentTexture();

        if (t != null) {
            g2d.drawSprite(t, pos.x - t.getWidth() / 2f, pos.y - t.getHeight() / 2f);
        }
    }
}
