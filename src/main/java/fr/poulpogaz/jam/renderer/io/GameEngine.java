package fr.poulpogaz.jam.renderer.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameEngine implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(GameEngine.class);

    public static final int TPS = 60;
    public static final float INV_TPS = 1 / 60f;

    private final IGame game;
    private final Window window;
    private final Input input;

    private boolean running = false;

    private int fps;
    private int tps;

    public GameEngine(IGame game, String title, int width, int height) {
        this.game = game;
        this.window = new Window(title, width, height);

        input = Input.getInstance();
    }

    public void init() throws Exception {
        window.createWindow();
        input.init(window.getID());
        game.init(window, this);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / TPS;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        int fps = 0;
        int tps = 0;

        running = true;

        window.show();

        final long windowID = window.getID();
        while (running && !glfwWindowShouldClose(windowID)) {
            long now = System.nanoTime();
            delta += (double) (now - lastTime) / ns;

            for (lastTime = now; delta >= 1.0; delta--) {
                tps++;
                game.update((float) delta);

                input.reset();
                glfwPollEvents();
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            game.render();
            glfwSwapBuffers(windowID);

            fps++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;

                this.fps = fps;
                this.tps = tps;
                LOGGER.info("FPS: {}; TPS: {}", fps, tps);

                fps = 0;
                tps = 0;
            }
        }

        game.terminate();
        input.free();
        window.close();
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public IGame getGame() {
        return game;
    }

    public int getFPS() {
        return fps;
    }

    public int getTPS() {
        return tps;
    }
}