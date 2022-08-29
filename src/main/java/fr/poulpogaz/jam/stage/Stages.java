package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.patterns.TargetPlayer;

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
                    .setStartPos(new EnemyScript.StartPos(0, EnemyScript.Location.TOP))
                    .setTriggerTime(0)
                    .addBulletPattern(0, new TargetPlayer(10))
                    .build()
                .build();
    }
}
