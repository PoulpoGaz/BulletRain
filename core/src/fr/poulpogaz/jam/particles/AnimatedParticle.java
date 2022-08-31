package fr.poulpogaz.jam.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimatedParticle extends Particle {

    private final Animation<TextureRegion> animation;

    public AnimatedParticle(Vector2 pos, Animation<TextureRegion> animation) {
        super(animation.getAnimationDuration());
        this.animation = animation;
        this.pos = pos;

    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        TextureRegion t = animation.getKeyFrame(timeAlive);

        batch.draw(t, pos.x - t.getRegionWidth() / 2f, pos.y - t.getRegionHeight() / 2f);
    }
}
