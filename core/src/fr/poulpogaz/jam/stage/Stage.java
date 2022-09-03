package fr.poulpogaz.jam.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.BackgroundRenderer;
import fr.poulpogaz.jam.Effect;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.entities.Boss;
import fr.poulpogaz.jam.entities.Bullet;
import fr.poulpogaz.jam.patterns.MovePattern;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Stage  {

    private final BackgroundRenderer background;
    private final Effect effect;

    private final Map<String, EnemyDescriptor> enemiesDescriptors;
    private final Map<String, IBulletDescriptor> bulletsDescriptors;
    private final List<Sequence> sequences;

    private final BossDescriptor boss;

    public Stage(BackgroundRenderer background,
                 Effect effect,
                 Map<String, EnemyDescriptor> enemiesDescriptors,
                 Map<String, IBulletDescriptor> bulletsDescriptors,
                 List<Sequence> sequences,
                 BossDescriptor boss) {
        this.background = background;
        this.effect = effect;
        this.enemiesDescriptors = enemiesDescriptors;
        this.bulletsDescriptors = bulletsDescriptors;
        this.sequences = sequences;
        this.boss = boss;
    }

    public void loadAllTextures() {
        for (EnemyDescriptor desc : enemiesDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        for (IBulletDescriptor desc : bulletsDescriptors.values()) {
            desc.renderer().loadTextures();
        }

        background.loadTextures();

        if (effect != null) {
            effect.loadTextures();
        }
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

    public BackgroundRenderer getBackground() {
        return background;
    }

    public Effect getEffect() {
        return effect;
    }

    public Collection<EnemyDescriptor> getEnemiesDescriptors() {
        return enemiesDescriptors.values();
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public BossDescriptor getBoss() {
        return boss;
    }
}
