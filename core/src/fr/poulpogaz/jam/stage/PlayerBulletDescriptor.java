package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.PlayerBullet;
import fr.poulpogaz.jam.patterns.MovePattern;

import java.util.Objects;

public final class PlayerBulletDescriptor implements IBulletDescriptor {
    private final String name;
    private final EntityRenderer renderer;
    private final HitBoxSupplier hitBoxSupplier;

    PlayerBulletDescriptor(String name,
                           EntityRenderer renderer,
                           HitBoxSupplier hitBoxSupplier) {
        this.name = name;
        this.renderer = renderer;
        this.hitBoxSupplier = hitBoxSupplier;
    }

    public String name() {
        return name;
    }

    public EntityRenderer renderer() {
        return renderer;
    }

    public HitBoxSupplier hitBoxSupplier() {
        return hitBoxSupplier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        PlayerBulletDescriptor that = (PlayerBulletDescriptor) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.renderer, that.renderer) &&
                Objects.equals(this.hitBoxSupplier, that.hitBoxSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, renderer, hitBoxSupplier);
    }

    @Override
    public String toString() {
        return "PlayerBulletDescriptor[" +
                "name=" + name + ", " +
                "renderer=" + renderer + ", " +
                "hitBoxSupplier=" + hitBoxSupplier + ']';
    }

    @Override
    public Bullet create(GameScreen game, boolean playerBullet, MovePattern pattern, Vector2 pos) {
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
