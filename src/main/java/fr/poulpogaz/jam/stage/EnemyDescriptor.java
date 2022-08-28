package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entity.EnemyRenderer;
import fr.poulpogaz.jam.entity.TextureEnemyRenderer;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.Objects;

public record EnemyDescriptor(EnemyRenderer renderer, int life) {


    public static class Builder {

        private final StageBuilder parent;

        private EnemyRenderer renderer;
        private int life;
        private String name;

        public Builder(StageBuilder parent) {
            this.parent = Objects.requireNonNull(parent);
        }

        public StageBuilder build() {
            Objects.requireNonNull(renderer);

            if (life <= 0) {
                throw new BuilderException("enemy is dead");
            }

            parent.addDescriptor(name, new EnemyDescriptor(renderer, life));

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
            return this;
        }

        public int getLife() {
            return life;
        }

        public Builder setLife(int life) {
            this.life = life;
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
