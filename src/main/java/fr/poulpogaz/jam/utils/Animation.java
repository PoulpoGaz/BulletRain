package fr.poulpogaz.jam.utils;

import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class Animation {

    private static final Logger LOGGER = LogManager.getLogger(Animation.class);

    private final AnimationDescriptor descriptor;
    private ITexture[] frames;
    private Texture texture;

    private int tick;
    private int index;

    private ITexture current;
    private boolean loop;

    public Animation(AnimationDescriptor desc) {
        if (!desc.hasFrame()) {
            throw new IllegalArgumentException("descriptor has no frame");
        }
        if (desc.getTexture() == null) {
            throw new IllegalArgumentException("No texture");
        }

        this.descriptor = desc;
    }

    public void loadTextures() throws IOException {
        texture = TextureCache.getOrCreate(descriptor.getTexture());

        List<AnimationDescriptor.Frame> f = descriptor.getFrames();

        frames = new ITexture[f.size()];

        for (int i = 0; i < f.size(); i++) {
            frames[i] = f.get(i).asTexture(texture);
        }
    }

    public void update() {
        if (!isFinished()) {
            checkLoaded();

            if (tick % descriptor.getDelay() == 0) {
                index++;

                if (loop && index >= frames.length) {
                    index = 0;
                }

                if (!isFinished()) {
                    current = frames[index];
                } else {
                    current = null;
                }
            }

            tick++;
        }
    }

    public ITexture getCurrentTexture() {
        checkLoaded();

        if (current == null && !isFinished()) {
            update();
        }

        return current;
    }

    private void checkLoaded() {
        if (texture == null) {
            LOGGER.warn("Texture not loaded for animation {}", descriptor.getName());
        }
    }

    public boolean isLoaded() {
        return texture != null;
    }

    public void reset() {
        tick = 0;
        current = null;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;

        if (loop && isFinished()) {
            index = 0;
        }
    }

    public boolean isLooping() {
        return loop;
    }

    public boolean isFinished() {
        return index >= frames.length;
    }

    public AnimationDescriptor getDescriptor() {
        return descriptor;
    }
}
