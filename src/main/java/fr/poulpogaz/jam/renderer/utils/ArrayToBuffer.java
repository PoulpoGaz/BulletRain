package fr.poulpogaz.jam.renderer.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ArrayToBuffer {

    public static IntBuffer asBuffer(int[] array) {
        return MemoryUtil.memAllocInt(array.length)
                .put(array)
                .flip();
    }

    public static FloatBuffer asBuffer(Vector2f[] vectors) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vectors.length * 2);

        for (Vector2f vector : vectors) {
            buffer.put(vector.x);
            buffer.put(vector.y);
        }

        return buffer.flip();
    }

    public static FloatBuffer asBuffer(Vector3f[] vectors) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vectors.length * 3);

        for (Vector3f vector : vectors) {
            buffer.put(vector.x);
            buffer.put(vector.y);
            buffer.put(vector.z);
        }

        return buffer.flip();
    }

    public static FloatBuffer asBuffer(Vector4f[] vectors) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vectors.length * 4);

        for (Vector4f vector : vectors) {
            buffer.put(vector.x);
            buffer.put(vector.y);
            buffer.put(vector.z);
            buffer.put(vector.w);
        }

        return buffer.flip();
    }
}