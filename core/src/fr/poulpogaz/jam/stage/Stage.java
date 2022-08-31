package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.patterns.MovePattern;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Stage  {

    // background texture
    private final String background;
    private final Map<String, EnemyDescriptor> enemiesDescriptors;
    private final Map<String, IBulletDescriptor> bulletsDescriptors;
    private final List<EnemyScript> scripts;

    public Stage(String background,
                 Map<String, EnemyDescriptor> enemiesDescriptors,
                 Map<String, IBulletDescriptor> bulletsDescriptors,
                 List<EnemyScript> scripts) {
        this.background = background;
        this.enemiesDescriptors = enemiesDescriptors;
        this.bulletsDescriptors = bulletsDescriptors;
        this.scripts = scripts;
        scripts.sort(Comparator.comparingDouble(EnemyScript::triggerTime));
    }

    public void loadAllTextures() {
        for (EnemyDescriptor desc : enemiesDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        for (IBulletDescriptor desc : bulletsDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        Jam.loadIfNeededTexture(background);
    }

    public Bullet createPlayerBullet(String name, GameScreen game, MovePattern movePattern, Vector2 pos) {
        return createBullet(name, game, true, movePattern, pos);
    }

    public Bullet createBullet(String name, GameScreen game, boolean playerBullet, MovePattern pattern, Vector2 pos) {
        IBulletDescriptor desc = bulletsDescriptors.get(name);
        if (desc == null) {
            Gdx.app.error("WARN", "No bullet descriptor named " + name + " found");
            return null;
        }

        return desc.create(game, playerBullet, pattern, pos);
    }

    public String getBackground() {
        return background;
    }

    public Collection<EnemyDescriptor> getEnemiesDescriptors() {
        return enemiesDescriptors.values();
    }

    public List<EnemyScript> getScripts() {
        return scripts;
    }
}
