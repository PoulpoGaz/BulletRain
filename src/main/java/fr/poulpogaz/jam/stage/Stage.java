package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.utils.TextureCache;
import fr.poulpogaz.jam.states.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Stage  {

    private static final Logger LOGGER = LogManager.getLogger(Stage.class);

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

    public void loadAllTextures() throws Exception {
        for (EnemyDescriptor desc : enemiesDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        for (IBulletDescriptor desc : bulletsDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        TextureCache.getOrCreate(background);
    }

    public Bullet createPlayerBullet(String name, Game game, MovePattern movePattern, Vector2f pos) {
        return createBullet(name, game, true, movePattern, pos);
    }

    public Bullet createBullet(String name, Game game, boolean playerBullet, MovePattern pattern, Vector2f pos) {
        IBulletDescriptor desc = bulletsDescriptors.get(name);
        if (desc == null) {
            LOGGER.warn("No bullet descriptor named {} found", name);
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
