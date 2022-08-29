package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.TextureEntityRenderer;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public record BulletDescriptor(String name, EntityRenderer renderer, HitBoxSupplier hitBoxSupplier, float damage) {


    public static class Builder {

        private final StageBuilder parent;

        private String name;
        private EntityRenderer renderer;
        private HitBoxSupplier hitBoxSupplier;
        private float damage;

        public Builder(StageBuilder parent) {
            this.parent = Objects.requireNonNull(parent);
        }

        public StageBuilder build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(renderer);
            Objects.requireNonNull(hitBoxSupplier);

            if (damage <= 0) {
                throw new BuilderException("Negative or 0 damage bullet");
            }

            return parent.addBullet(new BulletDescriptor(name, renderer, hitBoxSupplier, damage));
        }

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
