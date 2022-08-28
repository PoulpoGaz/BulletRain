package fr.poulpogaz.jam.stage;

import org.joml.Vector2f;

public class Stages {

    public static final Stage LEVEL_1;

    static {
        LEVEL_1 = new StageBuilder()
                .setBackground("desert_background.png")
                .descriptorBuilder()
                    .setName("sunflower")
                    .setTexture("tileset.png", 0, 0, 32, 32)
                    .setLife(100)
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(new Vector2f())
                    .build()
                .build();
    }
}
