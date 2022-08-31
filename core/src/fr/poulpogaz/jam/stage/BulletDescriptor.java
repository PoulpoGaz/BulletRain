package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public record BulletDescriptor(String name,
                               EntityRenderer renderer,
                               HitBoxSupplier hitBoxSupplier,
                               float damage) implements IBulletDescriptor {

    public Bullet create(GameScreen game, boolean playerBullet, MovePattern pattern, Vector2 pos) {
        return new Bullet(game, this, playerBullet, pattern, pos);
    }

    public static class Builder extends IBulletDescriptor.Builder {

        public Builder(StageBuilder parent) {
            super(parent);
        }

        @Override
        public StageBuilder build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(renderer);
            Objects.requireNonNull(hitBoxSupplier);

            if (damage <= 0) {
                throw new BuilderException("Negative or 0 damage bullet");
            }

            return parent.addBullet(new BulletDescriptor(name, renderer, hitBoxSupplier, damage));
        }
    }
}
