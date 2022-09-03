package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.engine.AABBRotateSupplier;
import fr.poulpogaz.jam.engine.AABBSupplier;
import fr.poulpogaz.jam.engine.CircleSupplier;
import fr.poulpogaz.jam.entities.TextureRotate;
import fr.poulpogaz.jam.patterns.*;

import static fr.poulpogaz.jam.Constants.*;
import static fr.poulpogaz.jam.stage.EnemyScript.Location;

public class Stages {

    private static final float M_8_W = MAP_WIDTH / 8f;
    private static final float M_3_8_W = 3 * MAP_WIDTH / 8f;
    private static final float M_5_8_W = 5 * MAP_WIDTH / 8f;
    private static final float M_7_8_W = 7 * MAP_WIDTH / 8f;

    private static final float M_8_H = MAP_HEIGHT / 8f;
    private static final float M_3_8_H = 3 * MAP_HEIGHT / 8f;
    private static final float M_5_8_H = 5 * MAP_HEIGHT / 8f;
    private static final float M_7_8_H = 7 * MAP_HEIGHT / 8f;

    public static final Stage LEVEL_1;

    static {
        LEVEL_1 = new StageBuilder()
                .setBackground("desert_background.png")

                // enemies
                .enemyBuilder()
                    .setName("sunflower")
                    .setTexture("tileset.png", 0, 0, 32, 32)
                    .setLife(20)
                    .setHitBox(new AABBSupplier(32, 32))
                    .setDropRate(0.5f)
                    .build()
                .enemyBuilder()
                    .setName("mosquito")
                    .setRotateTexture("tileset.png", 32, 0, 32, 32)
                    .setLife(20)
                    .setHitBox(new CircleSupplier(16))
                    .setDropRate(0.25f)
                    .build()
                .enemyBuilder()
                    .setName("dragon")
                    .setTexture("tileset.png", 64, 0, 53, 51)
                    .setLife(250)
                    .setHitBox(new AABBSupplier(53, 51))
                    .setDropRate(1)
                    .build()

                // bullets
                .subBuilder(PlayerBulletDescriptor.Builder::new)
                    .setName(PLAYER_BULLET_NAME)
                    .setRotateTexture("tileset.png", 32, 96, 10, 3)
                    .setHitBoxSupplier(new AABBRotateSupplier(10, 3))
                    .build()
                .bulletBuilder()
                    .setName("sunflower_bullet")
                    .setTexture("tileset.png", 2, 66, 12, 12)
                    .setDamage(1)
                    .setHitBoxSupplier(new CircleSupplier(6))
                    .build()
                .bulletBuilder()
                    .setName("fire_ball")
                    .setRenderer(new TextureRotate("tileset.png", 1, 50, 31, 12, 20.5f, 6))
                    .setDamage(1)
                    .setHitBoxSupplier(new AABBRotateSupplier(21, 12))
                    .build()

                // enemies
                /*.scriptBuilder("sunflower")
                    .setStartPos(0, Location.TOP)
                    .setTriggerTimeS(0)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()

                .scriptBuilder("sunflower")
                    .setStartPos(M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(5)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(-M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(5)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()

                .scriptBuilder("sunflower")
                    .setStartPos(-M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(16)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(-M_3_8_W, Location.TOP)
                    .setTriggerTimeS(16)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(M_3_8_W, Location.TOP)
                    .setTriggerTimeS(16)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(16)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()

                .scriptBuilder("mosquito")
                    .setStartPos(0, Location.LEFT)
                    .setTriggerTimeS(26)
                    .moveTo(new Vector2(-M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()
                .scriptBuilder("mosquito")
                    .setStartPos(20, Location.LEFT)
                    .setTriggerTimeS(26)
                    .moveTo(new Vector2(-M_3_8_W, M_7_8_H - 40), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()


                .scriptBuilder("mosquito")
                    .setStartPos(0, Location.RIGHT)
                    .setTriggerTimeS(29)
                    .moveTo(new Vector2(M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()
                .scriptBuilder("mosquito")
                    .setStartPos(20, Location.RIGHT)
                    .setTriggerTimeS(29)
                    .moveTo(new Vector2(M_3_8_W, M_7_8_H - 40), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()


                .scriptBuilder("sunflower")
                    .setStartPos(-M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(32)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()
                .scriptBuilder("sunflower")
                    .setStartPos(M_Q_WIDTH, Location.TOP)
                    .setTriggerTimeS(32)
                    .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                    .build()

                .scriptBuilder("mosquito")
                    .setStartPos(0, Location.LEFT)
                    .setTriggerTimeS(32)
                    .moveTo(new Vector2(-M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()
                .scriptBuilder("mosquito")
                    .setStartPos(0, Location.RIGHT)
                    .setTriggerTimeS(32)
                    .moveTo(new Vector2(M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                    .wait(60)
                    .addLastMove(new TargetMove.Player(8))
                    .build()*/

                .startSeq()
                    .scriptBuilder("dragon")
                        .setStartPos(0, Location.TOP)
                        .moveTo(new Vector2(0, HEIGHT - 150), 60)
                        .wait(900) // 20 sec
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                .endSeq()

                .startSeq()
                    .scriptBuilder("dragon")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(-M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1620) // 20 sec
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                    .scriptBuilder("dragon")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1620) // 20 sec
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("dragon")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(-M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1620) // 20 sec
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                    .scriptBuilder("dragon")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1620) // 20 sec
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                .endSeq(0)

                .startSeq()
                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H - 40), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H - 80), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H - 120), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()

                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H - 40), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H - 80), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H - 120), 20)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                .endSeq()

                .build();
    }
}
