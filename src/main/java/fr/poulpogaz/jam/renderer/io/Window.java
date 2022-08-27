package fr.poulpogaz.jam.renderer.io;

import fr.poulpogaz.jam.renderer.Static;
import fr.poulpogaz.jam.renderer.utils.GLDebugMessageCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final Logger LOGGER = LogManager.getLogger(Window.class);

    private long window;

    private final String title;
    private int width;
    private int height;

    private boolean fullscreen;

    private boolean resized = false;

    private int windowWidth;
    private int windowHeight;

    private int oldWidth;
    private int oldHeight;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void createWindow() {
        GLFWErrorCallback.create(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                LOGGER.warn("Error: {}, Description: {}", error, MemoryUtil.memUTF8(description));
            }
        }).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                resized = true;
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //noinspection ConstantConditions
        this.windowWidth = vidMode.width();
        this.windowHeight = vidMode.height();

        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);

        glEnable(GL43.GL_DEBUG_OUTPUT);
        GL43.glDebugMessageCallback(new GLDebugMessageCallback(), 0);
        glfwSwapInterval(0);

        Static.printInfoString();
    }

    public void show() {
        glfwShowWindow(window);
    }

    public void hide() {
        glfwHideWindow(window);
    }

    public void close() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getID() {
        return window;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            this.fullscreen = fullscreen;

            if (fullscreen) {
                oldWidth = width;
                oldHeight = height;

                glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, windowWidth, windowHeight, GLFW_DONT_CARE);
            } else {
                int x = (windowWidth - oldWidth) / 2;
                int y = (windowHeight - oldHeight) / 2;

                glfwSetWindowMonitor(window, NULL, x, y, oldWidth, oldHeight, GLFW_DONT_CARE);
            }
        }
    }
}