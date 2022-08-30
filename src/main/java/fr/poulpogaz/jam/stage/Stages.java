package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.engine.AABBSupplier;
import fr.poulpogaz.jam.engine.AABBRotateSupplier;
import fr.poulpogaz.jam.engine.CircleSupplier;
import fr.poulpogaz.jam.patterns.TargetPlayer;

import static fr.poulpogaz.jam.Constants.PLAYER_BULLET_NAME;
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
                    .setHitBox(new AABBSupplier(32, 32))
                    .build()

                // bullets
                .subBuilder(PlayerBulletDescriptor.Builder::new)
                    .setName(PLAYER_BULLET_NAME)
                    .setBulletTexture("tileset.png", 32, 96, 10, 3)
                    .setHitBoxSupplier(new AABBRotateSupplier(10, 3))
                    .build()
                .bulletBuilder()
                    .setName("sunflower_bullet")
                    .setTexture("tileset.png", 0, 64, 16, 16)
                    .setDamage(1)
                    .setHitBoxSupplier(new CircleSupplier(8))
                    .build()

                // enemies
                .scriptBuilder("sunflower")
                    .setStartPos(0, Location.TOP)
                    .setTriggerTime(0)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 45))
                    .build()

                .scriptBuilder("sunflower")
                    .setStartPos(Q_WIDTH, Location.TOP)
                    .setTriggerTime(200)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 45))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(-Q_WIDTH, Location.TOP)
                    .setTriggerTime(200)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 45))
                    .build()
                .build();
    }
}
