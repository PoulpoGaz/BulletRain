package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.utils.BuilderException;

import java.util.*;

public class StageBuilder {

    private String background;
    private final Map<String, EnemyDescriptor> enemyDescriptors = new HashMap<>();
    private final List<EnemyScript> scripts = new ArrayList<>();

    public Stage build() {
        Objects.requireNonNull(background, "no background set");

        if (enemyDescriptors.isEmpty()) {
            throw new BuilderException("No enemy");
        }

        return new Stage(background, enemyDescriptors, scripts);
    }

    public EnemyDescriptor.Builder descriptorBuilder() {
        return new EnemyDescriptor.Builder(this);
    }

    public StageBuilder addDescriptor(String name, EnemyDescriptor descriptor) {
        enemyDescriptors.put(name, descriptor);
        return this;
    }

    public EnemyScript.Builder scriptBuilder(String enemy) {
        EnemyDescriptor desc = enemyDescriptors.get(enemy);

        if (desc == null) {
            throw new BuilderException("No descriptor named " + enemy + " has been created");
        }

        return new EnemyScript.Builder(this, desc);
    }

    public StageBuilder addEnemyScript(EnemyScript script) {
        scripts.add(script);
        return this;
    }

    public String getBackground() {
        return background;
    }

    public StageBuilder setBackground(String background) {
        this.background = background;
        return this;
    }
}
