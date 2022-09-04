package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public class BulletDescriptor implements IBulletDescriptor {
    private final String name;
    private final EntityRenderer renderer;
    private final HitBoxSupplier hitBoxSupplier;
    private final float damage;

    BulletDescriptor(String name,
                     EntityRenderer renderer,
                     HitBoxSupplier hitBoxSupplier,
                     float damage) {
        this.name = name;
        this.renderer = renderer;
        this.hitBoxSupplier = hitBoxSupplier;
        this.damage = damage;
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

    public float damage() {
        return damage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        BulletDescriptor that = (BulletDescriptor) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.renderer, that.renderer) &&
                Objects.equals(this.hitBoxSupplier, that.hitBoxSupplier) &&
                Float.floatToIntBits(this.damage) == Float.floatToIntBits(that.damage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, renderer, hitBoxSupplier, damage);
    }

    @Override
    public String toString() {
        return "BulletDescriptor[" +
                "name=" + name + ", " +
                "renderer=" + renderer + ", " +
                "hitBoxSupplier=" + hitBoxSupplier + ", " +
                "damage=" + damage + ']';
    }

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

            return parent.addBullet(createDesc());
        }

        protected BulletDescriptor createDesc() {
            return new BulletDescriptor(name, renderer, hitBoxSupplier, damage);
        }
    }
}
