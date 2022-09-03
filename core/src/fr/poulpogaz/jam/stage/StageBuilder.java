package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entities.Boss;
import fr.poulpogaz.jam.utils.BuilderException;

import java.util.*;
import java.util.function.Function;

public class StageBuilder {

    private String background;
    private final Map<String, EnemyDescriptor> enemiesDescriptors = new HashMap<>();
    private final Map<String, IBulletDescriptor> bulletsDescriptors = new HashMap<>();
    private final List<Sequence> scripts = new ArrayList<>();

    private BossDescriptor boss;
    private List<EnemyScript> currSeq;

    public Stage build() {
        Objects.requireNonNull(background, "no background set");

        if (enemiesDescriptors.isEmpty()) {
            throw new BuilderException("No enemy");
        }

        return new Stage(background, enemiesDescriptors, bulletsDescriptors, scripts, boss);
    }

    public BulletDescriptor.Builder bulletBuilder() {
        return new BulletDescriptor.Builder(this);
    }


    public EnemyDescriptor.Builder enemyBuilder() {
        return new EnemyDescriptor.Builder(this);
    }

    public BossDescriptor.Builder bossBuilder() {
        return new BossDescriptor.Builder(this);
    }

    public EnemyScript.Builder scriptBuilder(String enemy) {
        if (currSeq == null) {
            throw new BuilderException("Sequence not started");
        }

        EnemyDescriptor desc = enemiesDescriptors.get(enemy);

        if (desc == null) {
            throw new BuilderException("No descriptor named " + enemy + " has been created");
        }

        return new EnemyScript.Builder(this, desc);
    }


    public StageBuilder addBullet(IBulletDescriptor bullet) {
        bulletsDescriptors.put(bullet.name(), bullet);
        return this;
    }

    public StageBuilder addEnemy(EnemyDescriptor descriptor) {
        enemiesDescriptors.put(descriptor.name(), descriptor);
        return this;
    }

    public StageBuilder startSeq() {
        if (currSeq != null) {
            throw new BuilderException("Sequence not ended");
        }

        currSeq = new ArrayList<>();
        return this;
    }

    public StageBuilder addEnemyScript(EnemyScript script) {
        if (currSeq == null) {
            throw new BuilderException("Sequence not started");
        }

        currSeq.add(script);
        return this;
    }

    public StageBuilder endSeq() {
        return endSeq(-1);
    }

    public StageBuilder endSeq(int wait) {
        scripts.add(new Sequence(currSeq, wait));
        currSeq = null;
        return this;
    }

    public <T extends BaseBuilder> T subBuilder(Function<StageBuilder, T> constructor) {
        return constructor.apply(this);
    }

    public String getBackground() {
        return background;
    }

    public StageBuilder setBackground(String background) {
        this.background = background;
        return this;
    }

    public BossDescriptor getBoss() {
        return boss;
    }

    public StageBuilder setBoss(BossDescriptor boss) {
        this.boss = boss;
        return this;
    }
}
