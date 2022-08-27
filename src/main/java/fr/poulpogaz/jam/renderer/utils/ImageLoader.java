package fr.poulpogaz.jam.renderer.utils;

import org.lwjgl.glfw.GLFWImage;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

public class ImageLoader {

    public static GLFWImage loadImage(String path) throws IOException {
        return loadImage(path, 128 * 128 * 4, true);
    }

    public static GLFWImage loadImage(String path, int bufferSize) throws IOException {
        return loadImage(path, bufferSize, true);
    }

    public static GLFWImage loadImage(String path, boolean resource) throws IOException {
        return loadImage(path, 128 * 128 * 4, resource);
    }

    public static GLFWImage loadImage(String path, int bufferSize, boolean resource) throws IOException {
        ByteBuffer buffer = Resource.toByteBuffer(path, bufferSize, resource);

        int[] w = new int[1];
        int[] h = new int[1];
        int[] comp = new int[1];

        if (!stbi_info_from_memory(buffer, w, h, comp)) {
            throw new IOException("Failed to read image information: " + stbi_failure_reason() + "; at " + path);
        }

        ByteBuffer pixels = stbi_load_from_memory(buffer, w, h, comp, STBI_rgb_alpha);
        if (pixels == null) {
            throw new IOException("Failed to load image: " + stbi_failure_reason() + "; at " + path);
        }

        return GLFWImage.create().set(w[0], h[0], pixels);
    }

    public static GLFWImage loadImage(ByteBuffer buffer) throws IOException {
        int[] w = new int[1];
        int[] h = new int[1];
        int[] comp = new int[1];

        if (!stbi_info_from_memory(buffer, w, h, comp)) {
            throw new IOException("Failed to read image information: " + stbi_failure_reason());
        }

        ByteBuffer pixels = stbi_load_from_memory(buffer, w, h, comp, STBI_rgb_alpha);
        if (pixels == null) {
            throw new IOException("Failed to load image: " + stbi_failure_reason());
        }

        return GLFWImage.create().set(w[0], h[0], pixels);
    }
}