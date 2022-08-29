package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.BulletRenderer;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.TextureEntityRenderer;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

import java.util.Objects;

public interface IBulletDescriptor {

    Bullet create(Game game, boolean playerBullet, MovePattern pattern, Vector2f pos);

    String name();

    EntityRenderer renderer();

    HitBoxSupplier hitBoxSupplier();

    float damage();


    abstract class Builder {

        protected final StageBuilder parent;

        protected String name;
        protected EntityRenderer renderer;
        protected HitBoxSupplier hitBoxSupplier;
        protected float damage;

        public Builder(StageBuilder parent) {
            this.parent = Objects.requireNonNull(parent);
        }

        public abstract StageBuilder build();

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public EntityRenderer getRenderer() {
            return renderer;
        }

        public Builder setRenderer(EntityRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder setTexture(String texture) {
            this.renderer = new TextureEntityRenderer(texture);
            return this;
        }

        public Builder setTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureEntityRenderer(texture, x, y, w, h);
            return this;
        }

        public Builder setBulletTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new BulletRenderer(texture, x, y, w, h);
            return this;
        }

        public HitBoxSupplier getHitBoxSupplier() {
            return hitBoxSupplier;
        }

        public Builder setHitBoxSupplier(HitBoxSupplier hitBoxSupplier) {
            this.hitBoxSupplier = hitBoxSupplier;
            return this;
        }

        public float getDamage() {
            return damage;
        }

        public Builder setDamage(float damage) {
            this.damage = damage;
            return this;
        }
    }
}