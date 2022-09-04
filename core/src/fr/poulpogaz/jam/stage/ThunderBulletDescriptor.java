package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.ThunderBullet;
import fr.poulpogaz.jam.patterns.MovePattern;

public class ThunderBulletDescriptor extends BulletDescriptor {

    public ThunderBulletDescriptor(String name, EntityRenderer renderer, HitBoxSupplier hitBoxSupplier, float damage) {
        super(name, renderer, hitBoxSupplier, damage);
    }

    @Override
    public Bullet create(GameScreen game, boolean playerBullet, MovePattern pattern, Vector2 pos) {
        return new ThunderBullet(game, this, playerBullet, pattern, pos);
    }

    public static class ThunderBuilder extends BulletDescriptor.Builder {

        public ThunderBuilder(StageBuilder parent) {
            super(parent);
        }

        @Override
        protected BulletDescriptor createDesc() {
            return new ThunderBulletDescriptor(name, renderer, hitBoxSupplier, damage);
        }
    }
}
