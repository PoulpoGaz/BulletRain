package fr.poulpogaz.jam.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.glClearColor;

public class GLUtils {

    public static void setClearColor(Vector4f color) {
        glClearColor(color.x, color.y, color.z, color.w);
    }

    public static void setClearColor(Vector3f color) {
        glClearColor(color.x, color.y, color.z, 1);
    }
}
