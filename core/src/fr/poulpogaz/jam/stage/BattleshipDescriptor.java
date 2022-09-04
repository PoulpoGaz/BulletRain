package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBoxSupplier;
import fr.poulpogaz.jam.entities.AbstractEnemy;
import fr.poulpogaz.jam.entities.Battleship;
import fr.poulpogaz.jam.entities.EntityRenderer;

public class BattleshipDescriptor extends EnemyDescriptor {

    public BattleshipDescriptor(String name, EntityRenderer renderer, HitBoxSupplier hitBox, int life, int width, int height, float dropRate) {
        super(name, renderer, hitBox, life, width, height, dropRate);
    }

    @Override
    public AbstractEnemy createEnemy(GameScreen screen, EnemyScript script) {
        return new Battleship(screen, script);
    }

    public static class BBuilder extends Builder {

        public BBuilder(StageBuilder parent) {
            super(parent);
        }

        @Override
        protected EnemyDescriptor createDescriptor() {
            return new BattleshipDescriptor(name, renderer, hitBox, life, width, height, dropRate);
        }
    }
}
