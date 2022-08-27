package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.utils.GLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector4f;

import java.util.HashMap;

public class StateManager {

    private static final StateManager INSTANCE = new StateManager();

    private static final Logger LOGGER = LogManager.getLogger(StateManager.class);

    private final HashMap<Class<?>, State> STATE_MAP;
    private State currentState;

    private Vector4f defaultClearColor = new Vector4f(0, 0, 0, 1);

    private boolean exit = false;

    private StateManager() {
        STATE_MAP = new HashMap<>();
    }

    public void loadStates() {
       // add(new Game());

        GLUtils.setClearColor(defaultClearColor);
    }

    public void update(float delta) {
        if (currentState != null) {
            currentState.update(delta);
        }
    }

    public void render(Graphics2D g2d, FontRenderer fr) {
        if (currentState != null) {
            currentState.render(g2d, fr);
        }
    }

    private void add(State state) {
        STATE_MAP.put(state.getClass(), state);
    }

    public State getState(Class<? extends State> class_) {
        return STATE_MAP.get(class_);
    }

    public void switchState(Class<? extends State> class_) throws Exception {
        State g = STATE_MAP.get(class_);

        if (g != null) {
            if (currentState != null) {
                currentState.hide();
            }
            currentState = g;
            currentState.show();

            if (currentState.getClearColor() != null) {
                GLUtils.setClearColor(currentState.getClearColor());
            } else {
                GLUtils.setClearColor(defaultClearColor);
            }

            LOGGER.info("Switching state to {}", class_);
        } else {
            LOGGER.info("Unknown state: {}", class_);
        }
    }

    public void exit() {
        exit = true;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean requireExit() {
        return exit;
    }

    public static StateManager getInstance() {
        return INSTANCE;
    }
}