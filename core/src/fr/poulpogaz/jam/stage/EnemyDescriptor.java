package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.*;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public class EnemyDescriptor {
    private final String name;
    private final EntityRenderer renderer;
    private final HitBoxSupplier hitBox;
    private final int life;
    private final int width;
    private final int height;
    private final float dropRate;

    EnemyDescriptor(String name, EntityRenderer renderer, HitBoxSupplier hitBox, int life, int width, int height, float dropRate) {
        this.name = name;
        this.renderer = renderer;
        this.hitBox = hitBox;
        this.life = life;
        this.width = width;
        this.height = height;
        this.dropRate = dropRate;
    }

    public AbstractEnemy createEnemy(GameScreen screen, EnemyScript script) {
        return new Enemy(screen, script);
    }

    public String name() {
        return name;
    }

    public EntityRenderer renderer() {
        return renderer;
    }

    public HitBoxSupplier hitBox() {
        return hitBox;
    }

    public int life() {
        return life;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float dropRate() {
        return dropRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnemyDescriptor that = (EnemyDescriptor) o;

        if (life != that.life) return false;
        if (width != that.width) return false;
        if (height != that.height) return false;
        if (Float.compare(that.dropRate, dropRate) != 0) return false;
        if (!name.equals(that.name)) return false;
        if (!renderer.equals(that.renderer)) return false;
        return hitBox.equals(that.hitBox);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + renderer.hashCode();
        result = 31 * result + hitBox.hashCode();
        result = 31 * result + life;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (dropRate != +0.0f ? Float.floatToIntBits(dropRate) : 0);
        return result;
    }

    public static class Builder extends BaseBuilder {

        protected EntityRenderer renderer;
        protected HitBoxSupplier hitBox;
        protected int life;
        protected int width;
        protected int height;
        protected float dropRate = 1;

        protected String name;

        public Builder(StageBuilder parent) {
            super(parent);
        }

        public StageBuilder build() {
            Objects.requireNonNull(renderer);
            Objects.requireNonNull(hitBox);
            Objects.requireNonNull(name);

            if (life <= 0) {
                throw new BuilderException("enemy is dead");
            }
            if (width <= 0) {
                throw new BuilderException("Negative or null width");
            }
            if (height <= 0) {
                throw new BuilderException("Negative or null height");
            }
            if (dropRate < 0) {
                throw new BuilderException("Negative drop rate");
            }

            return parent.addEnemy(createDescriptor());
        }

        protected EnemyDescriptor createDescriptor() {
            return new EnemyDescriptor(name, renderer, hitBox, life, width, height, dropRate);
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
            this.width = w;
            this.height = h;
            return this;
        }

        public Builder setRotateTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureRotate(texture, x, y, w, h);
            this.width = w;
            this.height = h;
            return this;
        }

        public HitBoxSupplier getHitBox() {
            return hitBox;
        }

        public Builder setHitBox(HitBoxSupplier hitBox) {
            this.hitBox = hitBox;
            return this;
        }

        public int getLife() {
            return life;
        }

        public Builder setLife(int life) {
            this.life = life;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public float getDropRate() {
            return dropRate;
        }

        public Builder setDropRate(float dropRate) {
            this.dropRate = dropRate;
            return this;
        }
    }
}
