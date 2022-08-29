package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.patterns.TargetPlayer;

import static fr.poulpogaz.jam.Constants.Q_WIDTH;
import static fr.poulpogaz.jam.stage.EnemyScript.Location;

public class Stages {

    public static final Stage LEVEL_1;

    static {
        LEVEL_1 = new StageBuilder()
                .setBackground("desert_background.png")

                // enemies
                .enemyBuilder()
                    .setName("sunflower")
                    .setTexture("tileset.png", 0, 0, 32, 32)
                    .setLife(100)
                    .build()

                // bullets

                // enemies
                .scriptBuilder("sunflower")
                    .setStartPos(0, Location.TOP)
                    .setTriggerTime(0)
                    .addBulletPattern(0, new TargetPlayer(10))
                    .build()

                .scriptBuilder("sunflower")
                    .setStartPos(Q_WIDTH, Location.TOP)
                    .setTriggerTime(200)
                    .addBulletPattern(0, new TargetPlayer(10))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(-Q_WIDTH, Location.TOP)
                    .setTriggerTime(200)
                    .addBulletPattern(0, new TargetPlayer(10))
                    .build()
                .build();
    }
}
