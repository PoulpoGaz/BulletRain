package fr.poulpogaz.jam;

import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.g2d.*;
import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.renderer.io.IGame;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.renderer.io.Window;
import fr.poulpogaz.jam.renderer.mesh.MultiMesh;
import fr.poulpogaz.jam.renderer.shaders.Shaders;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import fr.poulpogaz.jam.states.StateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Jam implements IGame {

    private static final Logger LOGGER = LogManager.getLogger(Jam.class);

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = DEFAULT_WIDTH * 4 / 3;

    public static final int WIDTH_SCALED = DEFAULT_WIDTH / 2;
    public static final int HEIGHT_SCALED = DEFAULT_HEIGHT / 2;

    private static final Jam INSTANCE = new Jam();

    private final Input input = Input.getInstance();

    private Window window;
    private GameEngine engine;

    private final Matrix4f projection2D = new Matrix4f().ortho2D(0, DEFAULT_WIDTH, DEFAULT_HEIGHT, 0);

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

        f2d = new FontRenderer(100);
        f2d.setProjection(projection2D);
        f2d.setFont(ImageFont.getOrCreate("dialog24iso8859_1", new Font("dialog", Font.PLAIN, 24), StandardCharsets.ISO_8859_1));

        TextureCache.getOrCreate("textures/tileset.png", true);

        stateManager.loadStates();
        stateManager.switchState(Game.class);
    }


    @Override
    public void render() {
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);

        g2d.setTransform(new Matrix4f().scale(2));
        stateManager.render(g2d, f2d);
        g2d.end();
    }

    @Override
    public void update(float delta) {
        if (window.isResized()) {
            //projection2D.setOrtho2D(0, window.getWidth(), window.getHeight(), 0);
            //g2d.setProjection(projection2D);

            glViewport((window.getWidth() - DEFAULT_WIDTH) / 2,
                    (window.getHeight() - DEFAULT_HEIGHT) / 2,
                    DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
        MultiMesh.disposeAll();
        renderer.dispose();
        g2d.dispose();
        OldFontRenderer.free();
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