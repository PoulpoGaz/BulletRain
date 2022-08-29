package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entity.EnemyRenderer;
import fr.poulpogaz.jam.entity.TextureEnemyRenderer;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public record EnemyDescriptor(String name, EnemyRenderer renderer, int life, int width, int height) {


    public static class Builder {

        private final StageBuilder parent;

        private EnemyRenderer renderer;
        private int life;
        private int width;
        private int height;

        private String name;

        public Builder(StageBuilder parent) {
            this.parent = Objects.requireNonNull(parent);
        }

        public StageBuilder build() {
            Objects.requireNonNull(renderer);

            if (life <= 0) {
                throw new BuilderException("enemy is dead");
            }
            if (width <= 0) {
                throw new BuilderException("Negative or null width");
            }
            if (height <= 0) {
                throw new BuilderException("Negative or null height");
            }

            parent.addDescriptor(name, new EnemyDescriptor(name, renderer, life, width, height));

            return parent;
        }

        public EnemyRenderer getRenderer() {
            return renderer;
        }

        public Builder setRenderer(EnemyRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder setTexture(String texture) {
            this.renderer = new TextureEnemyRenderer(texture);
            return this;
        }

        public Builder setTexture(String texture, int x, int y, int w, int h) {
            this.renderer = new TextureEnemyRenderer(texture, x, y, w, h);
            this.width = w;
            this.height = h;
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
