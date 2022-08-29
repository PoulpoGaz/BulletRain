package fr.poulpogaz.jam;

import fr.poulpogaz.jam.renderer.io.GameEngine;
import fr.poulpogaz.jam.utils.Log4j2Init;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Log4j2Init.init();
        LOGGER.info("=== STARTING JAM ===");

        System.setProperty("joml.debug", "true");
        System.setProperty("joml.format", "false");

        try (GameEngine engine = new GameEngine(Jam.getInstance(), "Jam", Jam.WINDOW_WIDTH, Jam.WINDOW_HEIGHT)) {
            try {
                engine.init();
            } catch (Exception e) {
                LOGGER.fatal("Can't initialize", e);
            }

            engine.run();
        }
    }
}