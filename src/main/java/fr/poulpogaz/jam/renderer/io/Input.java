package fr.poulpogaz.jam.renderer.io;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static final Input INSTANCE = new Input();

    private final boolean[] key_pressed = new boolean[GLFW_KEY_LAST];
    private final int[] key_pressed_tick = new int[GLFW_KEY_LAST];
    private final boolean[] key_released = new boolean[GLFW_KEY_LAST];

    private final boolean[] mouse_pressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private final boolean[] mouse_released = new boolean[GLFW_MOUSE_BUTTON_LAST];

    private float mouseX;
    private float mouseY;
    private float scroll;

    private char[] lastChar;

    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseCallback;
    private GLFWCursorPosCallback posCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWCharModsCallback charModsCallback;

    private Input() {
    }

    public void init(long window) {
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key < 0) {
                    return;
                }

                if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                    key_pressed[key] = true;
                    key_released[key] = false;
                } else if (action == GLFW_RELEASE) {
                    key_pressed[key] = false;
                    key_released[key] = true;

                    key_pressed_tick[key] = 0;
                }
            }
        };


        mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button < 0) {
                    return;
                }

                if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                    mouse_pressed[button] = true;
                    mouse_released[button] = false;
                } else if (action == GLFW_RELEASE) {
                    mouse_pressed[button] = false;
                    mouse_released[button] = true;
                }
            }
        };

        posCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = (float) xpos;
                mouseY = (float) ypos;
            }
        };

        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scroll = (float) yoffset;
            }
        };

        charModsCallback = new GLFWCharModsCallback() {
            @Override
            public void invoke(long window, int codepoint, int mods) {
                lastChar = Character.toChars(codepoint);
            }
        };

        glfwSetKeyCallback(window, keyCallback);
        glfwSetMouseButtonCallback(window, mouseCallback);
        glfwSetCursorPosCallback(window, posCallback);
        glfwSetScrollCallback(window, scrollCallback);
        glfwSetCharModsCallback(window, charModsCallback);
    }

    public boolean keyPressed(int key) {
        return key_pressed[key];
    }

    public boolean keyPressed(int key, int tickPeriod) {
        return key_pressed[key] && ((key_pressed_tick[key] % tickPeriod) == 0);
    }

    public boolean keyReleased(int key) {
        return key_released[key];
    }

    public boolean mousePressed(int key) {
        return mouse_pressed[key];
    }

    public boolean mouseReleased(int key) {
        return mouse_released[key];
    }

    void reset() {
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            if (!key_pressed[i]) {
                key_released[i] = false;
            } else {
                key_pressed_tick[i]++;
            }
        }

        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            if (!mouse_pressed[i]) {
                mouse_released[i] = false;
            }
        }

        scroll = 0;

        lastChar = null;
    }

    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    public GLFWMouseButtonCallback getMouseCallback() {
        return mouseCallback;
    }

    public GLFWCursorPosCallback getPosCallback() {
        return posCallback;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public float getScroll() {
        return scroll;
    }

    public char[] getLastChar() {
        return lastChar;
    }

    void free() {
        keyCallback.free();
        mouseCallback.free();
        posCallback.free();
        scrollCallback.free();
        charModsCallback.free();
    }

    public static Input getInstance() {
        return INSTANCE;
    }
}