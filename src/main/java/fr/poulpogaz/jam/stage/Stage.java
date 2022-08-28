package fr.poulpogaz.jam.stage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Stage {

    // background texture
    private String background;
    private Map<String, EnemyDescriptor> descriptors;

    // ordered by startpos.y
    private List<EnemyScript> scripts;

    public Stage(String background, Map<String, EnemyDescriptor> descriptors, List<EnemyScript> scripts) {
        this.background = background;
        this.descriptors = descriptors;
        this.scripts = scripts;
        scripts.sort(Comparator.comparingDouble((e) -> e.startPos().y));
    }

    public String getBackground() {
        return background;
    }

    public Collection<EnemyDescriptor> getDescriptors() {
        return descriptors.values();
    }

    public List<EnemyScript> getScripts() {
        return scripts;
    }
}
