package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.entities.TextureRotate;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.TextureEntityRenderer;
import fr.poulpogaz.jam.patterns.MovePattern;

public interface IBulletDescriptor {

    Bullet create(GameScreen game, boolean playerBullet, MovePattern pattern, Vector2 pos);

    String name();

    EntityRenderer renderer();

    HitBoxSupplier hitBoxSupplier();

    float damage();


    abstract class Builder extends BaseBuilder {

        protected String name;
        protected EntityRenderer renderer;
        protected HitBoxSupplier hitBoxSupplier;
        protected float damage;

        public Builder(StageBuilder parent) {
            super(parent);
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

        public Builder setRotateTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureRotate(texture, x, y, w, h);
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
