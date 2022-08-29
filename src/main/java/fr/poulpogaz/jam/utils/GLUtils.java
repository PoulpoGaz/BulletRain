package fr.poulpogaz.jam.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class GLUtils {

    private static boolean blendEnabled = false;

    public static void setClearColor(Vector4f color) {
        glClearColor(color.x, color.y, color.z, color.w);
    }

    public static void setClearColor(Vector3f color) {
        glClearColor(color.x, color.y, color.z, 1);
    }

    public static void blend() {
        if (!blendEnabled) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            blendEnabled = true;
        }
    }

    public static void noBlend() {
        if (blendEnabled) {
            glDisable(GL_BLEND);
            blendEnabled = false;
        }
    }

    public static void setBlendEnabled(boolean enabled) {
        if (blendEnabled != enabled) {
            if (enabled) {
                blend();
            } else {
                noBlend();;
            }
        }
    }

    public static boolean isBlendEnabled() {
        return blendEnabled;
    }
}
