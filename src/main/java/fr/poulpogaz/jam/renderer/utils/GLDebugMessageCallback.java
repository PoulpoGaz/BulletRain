package fr.poulpogaz.jam.renderer.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryUtil;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;

public class GLDebugMessageCallback extends org.lwjgl.opengl.GLDebugMessageCallback {

    private static final Logger LOGGER = LogManager.getLogger(GLDebugMessageCallback.class);

    private static final Map<Integer, String> NAMES;

    static {
        NAMES = new HashMap<>();
        NAMES.put(0x8246, "GL_DEBUG_SOURCE_API");
        NAMES.put(0x8247, "GL_DEBUG_SOURCE_WINDOW_SYSTEM");
        NAMES.put(0x8248, "GL_DEBUG_SOURCE_SHADER_COMPILER");
        NAMES.put(0x8249, "GL_DEBUG_SOURCE_THIRD_PARTY");
        NAMES.put(0x824A, "GL_DEBUG_SOURCE_APPLICATION");

        NAMES.put(0x824C, "GL_DEBUG_TYPE_ERROR");
        NAMES.put(0x824D, "GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR");
        NAMES.put(0x824E, "GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR ");
        NAMES.put(0x824F, "GL_DEBUG_TYPE_PORTABILITY");
        NAMES.put(0x8250, "GL_DEBUG_TYPE_PERFORMANCE");
        NAMES.put(0x8251, "GL_DEBUG_TYPE_OTHER");
        NAMES.put(0x8268, "GL_DEBUG_TYPE_MARKER");

        NAMES.put(0x9146, "GL_DEBUG_SEVERITY_HIGH");
        NAMES.put(0x9147, "GL_DEBUG_SEVERITY_MEDIUM");
        NAMES.put(0x9148, "GL_DEBUG_SEVERITY_LOW");
        NAMES.put(0x826B, "GL_DEBUG_SEVERITY_NOTIFICATION");
    }

    @Override
    public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
        if (severity == GL_DEBUG_SEVERITY_NOTIFICATION) {
            LOGGER.info("Source={}, type={}, id={}, severity={}, message={}",
                    NAMES.get(source),
                    NAMES.get(type),
                    id,
                    NAMES.get(severity),
                    MemoryUtil.memUTF8(message, length));
        } else {
            LOGGER.warn("Source={}, type={}, id={}, severity={}, message={}",
                    NAMES.get(source),
                    NAMES.get(type),
                    id,
                    NAMES.get(severity),
                    MemoryUtil.memUTF8(message, length),
                    new Exception());
        }
    }
}