package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.Edge;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.engine.MultiHitBox;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.stage.IBulletDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ThunderBullet extends Bullet {

    private final List<Vector2> hits = new ArrayList<>();

    public ThunderBullet(GameScreen game,
                         IBulletDescriptor descriptor,
                         boolean playerBullet,
                         MovePattern movePattern,
                         Vector2 pos) {
        super(game, descriptor, playerBullet, movePattern, pos);
    }

    public List<Vector2> getHits() {
        return hits;
    }

    public static class HBSupplier implements HitBoxSupplier {

        @Override
        public HitBox getDetailedHitBox(Entity entity, HitBox last) {
            if (last instanceof MultiHitBox) {
                ThunderBullet t = (ThunderBullet) entity;
                MultiHitBox box = (MultiHitBox) last;

                if (t.hits.isEmpty()) {
                    return last;
                }

                if (box.getBoxes().size != t.hits.size()) {
                    Vector2 a;
                    Vector2 b;

                    a = t.hits.get(t.hits.size() - 2);
                    b = t.hits.get(t.hits.size() - 1);

                    Edge e = new Edge();

                    e.getA().set(a);
                    e.getB().set(b);

                    box.getBoxes().add(e);

                }

                return last;
            } else {
                return new MultiHitBox();
            }
        }
    }
}
