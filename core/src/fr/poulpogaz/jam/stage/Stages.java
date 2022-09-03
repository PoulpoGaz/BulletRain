package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.BackgroundRenderer;
import fr.poulpogaz.jam.RainEffect;
import fr.poulpogaz.jam.engine.AABBRotateSupplier;
import fr.poulpogaz.jam.engine.AABBSupplier;
import fr.poulpogaz.jam.engine.CircleSupplier;
import fr.poulpogaz.jam.entities.TextureRotate;
import fr.poulpogaz.jam.patterns.*;

import java.util.ArrayList;
import java.util.List;

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

    public static final List<Stage> STAGES;

    public static final Stage LEVEL_1;
    public static final Stage LEVEL_2;

    static {
        LEVEL_1 = new StageBuilder()
                .setBackground("desert_background.png")

                // enemies
                .enemyBuilder()
                    .setName("sunflower")
                    .setTexture("tileset.png", 0, 0, 32, 32)
                    .setLife(35)
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
                    .setLife(1250)
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
                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(0, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_3_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_3_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.LEFT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(-M_3_8_W, M_7_8_H - 40), 20) // M_8_W, M_7_8_H), 10)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                .endSeq(60)


                .startSeq()
                    .scriptBuilder("mosquito")
                        .setStartPos(0, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H), 20) // M_8_W, M_7_8_H), 10)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                    .scriptBuilder("mosquito")
                        .setStartPos(20, Location.RIGHT)
                        .setTriggerTimeS(0)
                        .moveTo(new Vector2(M_3_8_W, M_7_8_H - 40), 20) // M_8_W, M_7_8_H), 10)
                        .wait(60)
                        .addLastMove(new TargetMove.Player(8))
                        .build()
                .endSeq(60)


                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 64, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 64, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()

                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 64, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 64, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                .endSeq(60)


                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()

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
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 32, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 64, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 96, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 32, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 64, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 96, Location.TOP)
                        .setTriggerTimeS(0)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()

                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 32, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 64, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(-M_8_W - 96, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 32, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 64, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                    .scriptBuilder("sunflower")
                        .setStartPos(M_8_W + 96, Location.TOP)
                        .setTriggerTimeS(2)
                        .addBulletPattern(0, new TargetPlayer("sunflower_bullet", 60))
                        .build()
                .endSeq(60)

                .startSeq()
                    .scriptBuilder("dragon")
                        .setStartPos(0, Location.TOP)
                        .moveTo(new Vector2(0, HEIGHT - 150), 60)
                        .wait(900)
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                .endSeq()

                .startSeq()
                    .scriptBuilder("dragon")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(-M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1200)
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()
                    .scriptBuilder("dragon")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1200)
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .build()

                    .scriptBuilder("dragon")
                        .setStartPos(-M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(-M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1200)
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .setTriggerTimeS(8)
                        .build()
                    .scriptBuilder("dragon")
                        .setStartPos(M_Q_WIDTH, Location.TOP)
                        .moveTo(new Vector2(M_Q_WIDTH, HEIGHT - 150), 60)
                        .wait(1200)
                        .addLastMove(MovePattern.ANTI_FOLLOW_MAP)
                        .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                        .setTriggerTimeS(8)
                        .build()
                .endSeq(0)

                .bossBuilder()
                    .setLife(7500)
                    .setTexture("tileset.png", 132, 1, 24, 30)
                    .setHitBox(new AABBSupplier(24, 30))

                    .newPhase()
                    .addBulletPattern(0, new BulletRainPattern("fire_ball", 8, new Vector2(-1, -1), 3f, 5f))
                    .wait(Integer.MAX_VALUE)

                    .newPhase(5000)
                    .addLastMove(new CircularMove(new Vector2(0, -100),  5))
                    .addBulletPattern(0, new MultiSpiralPattern("fire_ball", 10, 30))

                    .newPhase(2500)
                    .addLastMove(new RandomMovePulse(90))
                    .addBulletPattern(120, new MultiSpiralPattern("fire_ball", 10, 30))
                    .build()
                .build();


        LEVEL_2 = new StageBuilder()
                .setBackground(new BackgroundRenderer.Tex("water_background.png"))
                .setEffect(new RainEffect())
                .build();


        STAGES = new ArrayList<>();
        STAGES.add(LEVEL_1);
        STAGES.add(LEVEL_2);
    }
}
