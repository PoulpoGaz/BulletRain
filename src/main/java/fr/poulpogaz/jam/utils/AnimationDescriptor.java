package fr.poulpogaz.jam.utils;

import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.SubTexture;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.json.tree.JsonArray;
import fr.poulpogaz.json.tree.JsonElement;
import fr.poulpogaz.json.tree.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AnimationDescriptor {

    public static AnimationDescriptor fromJson(String name, JsonObject object) {
        int delay = object.getOptionalInt("delay").orElse(5);
        String texture = object.getAsString("texture");

        List<Frame> frames = new ArrayList<>();
        JsonArray f = object.getAsArray("frames");

        for (JsonElement e : f) {
            JsonObject o = (JsonObject) e;
            int x = o.getAsInt("x");
            int y = o.getAsInt("y");
            int w = o.getAsInt("w");
            int h = o.getAsInt("h");

            frames.add(new Frame(x, y, w, h));
        }

        return new AnimationDescriptor(name, frames, texture, delay);
    }

    private final String name;
    private List<Frame> frames;
    private String texture;
    private int delay; // delay between two frame

    public AnimationDescriptor(String name) {
        this.name = name;
        frames = new ArrayList<>();
        delay = 0;
    }

    public AnimationDescriptor(String name, List<Frame> frames, String texture, int delay) {
        this.name = name;
        this.frames = frames;
        this.texture = texture;
        this.delay = delay;
    }

    public Animation createAnimation() {
        return new Animation(this);
    }

    public boolean hasFrame() {
        return !frames.isEmpty();
    }

    public String getName() {
        return name;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public String getTexture() {
        return texture;
    }

    public int getDelay() {
        return delay;
    }

    public int getDuration() {
        return delay * frames.size();
    }

    public record Frame(int x, int y, int w, int h) {

        public ITexture asTexture(Texture tex) {
            if (x == 0 && y == 0 && w == tex.getWidth() && h == tex.getHeight()) {
                return tex;
            } else {
                return new SubTexture(x, y, w, h, tex);
            }
        }
    }
}
