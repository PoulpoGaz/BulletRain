package fr.poulpogaz.jam;

import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.g2d.*;
import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.renderer.io.IGame;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.renderer.io.Window;
import fr.poulpogaz.jam.renderer.shaders.Shaders;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.states.StateManager;
import fr.poulpogaz.jam.utils.Animations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.opengl.GL11.*;

public class Jam implements IGame {

    private static final Logger LOGGER = LogManager.getLogger(Jam.class);

    public static final int SCALE_FACTOR = 2;

    public static final int WINDOW_WIDTH = Constants.WIDTH * SCALE_FACTOR;
    public static final int WINDOW_HEIGHT = Constants.HEIGHT * SCALE_FACTOR;

    private static final Jam INSTANCE = new Jam();

    private final Input input = Input.getInstance();

    private Window window;
    private GameEngine engine;

    private final Matrix4f projection2D = new Matrix4f().ortho2D(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0);
    private final Matrix4f scale = new Matrix4f().scale(2);

    private Renderer2D renderer;
    private Graphics2D g2d;
    private FontRenderer f2d;

    private final StateManager stateManager = StateManager.getInstance();

    private Jam() {

    }

    @Override
    public void init(Window window, GameEngine engine) throws Exception {
        this.window = window;
        this.engine = engine;

        Shaders.init();
        renderer = new Renderer2D(5000, 50000);
        g2d = new Graphics2D(renderer);
        g2d.setProjection(projection2D);
        g2d.setTransform(scale);

        f2d = new FontRenderer(500);
        f2d.setProjection(projection2D);
        f2d.setFont(ImageFont.getOrCreate("dialog24iso8859_1", new Font("dialog", Font.PLAIN, 24), StandardCharsets.ISO_8859_1));
        f2d.setColor(Colors.WHITE);

        Animations.loadAnimations();
        TextureCache.getOrCreate("tileset.png");

        stateManager.loadStates();
        stateManager.switchState(Game.class);
    }


    @Override
    public void render() {
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);

        g2d.setTransform(scale);
        stateManager.render(g2d, f2d);
        g2d.end();
        f2d.flush();
    }

    @Override
    public void update(float delta) {
        if (window.isResized()) {
            glViewport((window.getWidth() - WINDOW_WIDTH) / 2,
                    (window.getHeight() - WINDOW_HEIGHT) / 2,
                    WINDOW_WIDTH, WINDOW_HEIGHT);
            window.setResized(false);
        }

        if (input.keyReleased(GLFW_KEY_F11)) {
            window.setFullscreen(!window.isFullscreen());
        }

        if (input.keyReleased(GLFW_KEY_ESCAPE)) {
            engine.stop();
            return;
        }

        stateManager.update(delta);
    }

    @Override
    public void terminate() {
        g2d.close();
        renderer.close();
        f2d.getFont().close();
        f2d.close();

        Shaders.dispose();
        TextureCache.free();
    }

    public static Jam getInstance() {
        return INSTANCE;
    }

    public Input getInput() {
        return input;
    }

    public Graphics2D getG2D() {
        return g2d;
    }

    public Renderer2D getRenderer2D() {
        return renderer;
    }
}