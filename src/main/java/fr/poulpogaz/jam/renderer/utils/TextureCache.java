package fr.poulpogaz.jam.renderer.utils;

import fr.poulpogaz.jam.renderer.Texture;
import org.lwjgl.glfw.GLFWImage;

import java.io.IOException;
import java.util.HashMap;

public class TextureCache {

    private static final HashMap<String, Texture> CACHE = new HashMap<>();

    public static void put(String path, Texture texture) {
        CACHE.put(path, texture);
    }

    public static Texture get(String path) {
        return CACHE.get(path);
    }

    public static Texture getOrCreate(String path, boolean resource) throws IOException {
        Texture texture = CACHE.get(path);

        if (texture != null) {
            return texture;
        }

        GLFWImage image = ImageLoader.loadImage(path, resource);
        texture = new Texture(image);

        CACHE.put(path, texture);

        return texture;
    }

    public static Texture getOrCreate(String path) throws IOException {
        return getOrCreate(path, true);
    }

    public static void free() {
        CACHE.values().forEach(Texture::dispose);
    }
}