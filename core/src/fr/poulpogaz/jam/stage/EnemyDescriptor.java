package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.EntityRenderer;
import fr.poulpogaz.jam.entities.TextureEntityRenderer;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public final class EnemyDescriptor {
    private final String name;
    private final EntityRenderer renderer;
    private final HitBoxSupplier hitBox;
    private final int life;
    private final int width;
    private final int height;

    EnemyDescriptor(String name, EntityRenderer renderer, HitBoxSupplier hitBox, int life, int width, int height) {
        this.name = name;
        this.renderer = renderer;
        this.hitBox = hitBox;
        this.life = life;
        this.width = width;
        this.height = height;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        EnemyDescriptor that = (EnemyDescriptor) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.renderer, that.renderer) &&
                Objects.equals(this.hitBox, that.hitBox) &&
                this.life == that.life &&
                this.width == that.width &&
                this.height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, renderer, hitBox, life, width, height);
    }

    @Override
    public String toString() {
        return "EnemyDescriptor[" +
                "name=" + name + ", " +
                "renderer=" + renderer + ", " +
                "hitBox=" + hitBox + ", " +
                "life=" + life + ", " +
                "width=" + width + ", " +
                "height=" + height + ']';
    }


    public static class Builder extends BaseBuilder {

        private EntityRenderer renderer;
        private HitBoxSupplier hitBox;
        private int life;
        private int width;
        private int height;

        private String name;

        public Builder(StageBuilder parent) {
            super(parent);
        }

        public StageBuilder build() {
            Objects.requireNonNull(renderer);
            Objects.requireNonNull(hitBox);

            if (life <= 0) {
                throw new BuilderException("enemy is dead");
            }
            if (width <= 0) {
                throw new BuilderException("Negative or null width");
            }
            if (height <= 0) {
                throw new BuilderException("Negative or null height");
            }

            return parent.addEnemy(new EnemyDescriptor(name, renderer, hitBox, life, width, height));
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
    }
}
