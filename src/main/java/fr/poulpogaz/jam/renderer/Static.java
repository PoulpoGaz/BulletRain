package fr.poulpogaz.jam.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.Version.getVersion;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL30.GL_NUM_EXTENSIONS;
import static org.lwjgl.opengl.GL30.glGetStringi;

public class Static {

    private static final Logger LOGGER = LogManager.getLogger(Static.class);

    public static void printInfoString() {
        LOGGER.info("""
                                                
                            ---------OpenGL info---------
                            Vendor:                   {}
                            Renderer:                 {}
                            Version:                  {}
                            Shading Language Version: {}
                            Extensions:               {}
                            ---------LWJGL info---------
                            Version:                  {}""",
                    glGetString(GL_VENDOR),
                    glGetString(GL_RENDERER),
                    glGetString(GL_VERSION),
                    glGetString(GL_SHADING_LANGUAGE_VERSION),
                    getExtensions(),
                    getVersion());
    }

    public static String infoString() {
        return String.format("""
                                     ---------OpenGL info---------
                                     Vendor:                   %s
                                     Renderer:                 %s
                                     Version:                  %s
                                     Shading Language Version: %s
                                     Extensions:               %s
                                     ---------LWJGL info---------
                                     Version:                  %s""",
                             glGetString(GL_VENDOR),
                             glGetString(GL_RENDERER),
                             glGetString(GL_VERSION),
                             glGetString(GL_SHADING_LANGUAGE_VERSION),
                             getExtensions(),
                             getVersion());
    }

    private static String getExtensions() {
        int max = glGetInteger(GL_NUM_EXTENSIONS);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < max; i++) {
            builder.append(glGetStringi(GL_EXTENSIONS, i));

            if (i < max - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}
