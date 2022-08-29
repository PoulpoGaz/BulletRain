package fr.poulpogaz.jam.utils;

import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.tree.JsonObject;
import fr.poulpogaz.json.tree.JsonTreeReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Animations {

    private static final Logger LOGGER = LogManager.getLogger(Animations.class);

    private static final Map<String, AnimationDescriptor> ANIMATIONS = new HashMap<>();

    public static void loadAnimations() throws IOException, JsonException {
        load("hit_animation");
    }

    private static void load(String name) throws IOException, JsonException {
        LOGGER.debug("Loading animation: {}", name);

        try (InputStream is = Animations.class.getResourceAsStream("/animations/" + name + ".json")) {
            if (is == null) {
                throw new IOException("Can't find animation: " + name);
            }

            JsonObject obj = (JsonObject) JsonTreeReader.read(is);
            ANIMATIONS.put(name, AnimationDescriptor.fromJson(name, obj));
        }
    }

    public static AnimationDescriptor get(String name) {
        return ANIMATIONS.get(name);
    }
}
