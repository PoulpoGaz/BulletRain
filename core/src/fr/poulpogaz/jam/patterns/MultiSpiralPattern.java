package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

import java.util.function.Function;

public class MultiSpiralPattern implements BulletPattern {

    private final String bullet;
    private final int nBranch;
    private final int reloadTick;
    private final Function<Vector2, MovePattern> move;

    private int tick;

    private final float theta;
    private float offset;

    public MultiSpiralPattern(String bullet, int nBranch, int reloadTick) {
        this(bullet, nBranch, reloadTick, LinearPattern::new);
    }

    public MultiSpiralPattern(String bullet, int nBranch, int reloadTick, Function<Vector2, MovePattern> move) {
        this.bullet = bullet;
        this.nBranch = nBranch;
        this.reloadTick = reloadTick;
        this.move = move;

        theta = 2 * Mathf.PI / nBranch;
    }

    @Override
    public void addBullets(GameScreen game, Entity entity, boolean player) {
        if (tick == 0) {
            Stage s = game.getStage();

            Vector2 rot = new Vector2(0, 1);

            float t = offset;
            for (int i = 0; i < nBranch; i++) {
                Vector2 dir = rot.cpy().rotateRad(t);

                game.addBullet(s.createBullet(bullet, game, false, move.apply(dir.scl(1.5f)), entity.getPos().cpy()));

                t += theta;
            }

            offset += 0.2f;
        }

        tick++;
        if (tick >= reloadTick) {
            tick = 0;
        }
    }
}
