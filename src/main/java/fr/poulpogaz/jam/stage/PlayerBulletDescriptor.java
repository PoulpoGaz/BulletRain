package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.PlayerBullet;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import java.util.Objects;

public record PlayerBulletDescriptor(String name,
                                     EntityRenderer renderer,
                                     HitBoxSupplier hitBoxSupplier) implements IBulletDescriptor {

    @Override
    public Bullet create(Game game, boolean playerBullet, MovePattern pattern, Vector2f pos) {
        return new PlayerBullet(game, this, playerBullet, pattern, pos);
    }

    @Override
    public float damage() {
        return 0;
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

            return parent.addBullet(new PlayerBulletDescriptor(name, renderer, hitBoxSupplier));
        }
    }
}
