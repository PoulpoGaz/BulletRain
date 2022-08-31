package fr.poulpogaz.jam.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.tree.JsonArray;
import fr.poulpogaz.json.tree.JsonElement;
import fr.poulpogaz.json.tree.JsonObject;
import fr.poulpogaz.json.tree.JsonTreeReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animations {

    private static final Map<String, Animation<TextureRegion>> ANIMATIONS = new HashMap<>();

    public static void loadAnimations() {
        if (ANIMATIONS.isEmpty()) {
            try {
                load("hit_animation");
                load("expl_08_animation");
            } catch (IOException | JsonException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void load(String name) throws IOException, JsonException {
        Gdx.app.debug("DEBUG", "Loading animation: " + name);

        try (InputStream is = Animations.class.getResourceAsStream("/animations/" + name + ".json")) {
            if (is == null) {
                throw new IOException("Can't find animation: " + name);
            }

            Array<TextureRegion> frames = new Array<>();
            JsonObject obj = (JsonObject) JsonTreeReader.read(is);

            float delay = obj.getOptionalFloat("delay").orElse(1/30f);
            String texture = obj.getAsString("texture");

            Texture t = Jam.getTexture(texture);

            JsonArray f = obj.getAsArray("frames");

            for (JsonElement e : f) {
                JsonObject o = (JsonObject) e;
                int x = o.getAsInt("x");
                int y = o.getAsInt("y");
                int w = o.getAsInt("w");
                int h = o.getAsInt("h");

                frames.add(new TextureRegion(t, x, y, w, h));
            }

            Animation<TextureRegion> anim = new Animation<>(delay, frames);
            ANIMATIONS.put(name, anim);
        }
    }

    public static Animation<TextureRegion> get(String name) {
        return ANIMATIONS.get(name);
    }
}
