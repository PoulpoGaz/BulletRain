package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.entities.ThunderBullet;
import fr.poulpogaz.jam.entities.Entity;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.utils.Mathf;

import java.util.List;

public class ThunderPattern extends AbstractMovePattern {

    private static final float ADD = 10f;

    private final Vector2 source = new Vector2();
    private final Vector2 dest = new Vector2();
    private final Vector2 dir = new Vector2();

    private int nLine = 0;
    private float len;

    @Override
    public void init(GameScreen game, Entity entity, int tStart) {
        source.set(entity.getPos());
        dest.set(game.getPlayer().getPos());
        dir.set(dest).sub(source);

        nLine = (int) (dir.len() / ADD);
        dir.nor();

        len = 0;
    }

    @Override
    public void dirImpl(int t, Vector2 last, GameScreen screen, Entity entity) {
        if (entity instanceof ThunderBullet) {
            ThunderBullet c = (ThunderBullet) entity;

            if (t % 2 == 0) {
                List<Vector2> hits = c.getHits();

                if (hits == null) {
                    return;
                }

                if (hits.isEmpty()) {
                    hits.add(source.cpy());
                }

                if (hits.size() + 1 == nLine) {
                    hits.add(dest);
                    c.markDirty();
                } else if (hits.size() >= nLine) {
                    c.setExploded(true);
                    c.setExplosionPos(hits.get(hits.size() - 1));
                } else {
                    len += ADD;

                    Vector2 next = dir.cpy().scl(len);
                    float deviation = Mathf.random(10, 20);

                    // take the perpendicular
                    next.add(-dir.y * deviation, dir.x * deviation);
                    next.add(source);

                    Gdx.app.debug("DEBUG", "next= " + next + ", source=" + source + ", dest=" + dest + ", dir=" + dir);

                    hits.add(next);
                    c.markDirty();
                }
            }
        }
    }

    public static class Bullet implements BulletPattern {

        private final String name;
        private int tick = 0;

        public Bullet(String name) {
            this.name = name;
        }

        @Override
        public void addBullets(GameScreen game, Entity entity, boolean player) {
            if (tick == 0) {
                Stage s = game.getStage();
                game.addBullet(s.createBullet(name, game, false, new ThunderPattern(), entity.getPos().cpy()));
            }

            tick++;
            if (tick >= 60) {
                tick = 0;
            }
        }
    }
}
