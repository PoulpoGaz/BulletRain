package fr.poulpogaz.jam.stage;

import java.util.List;

public class Sequence {

    private final List<EnemyScript> scripts;
    private final int wait;

    public Sequence(List<EnemyScript> scripts, int wait) {
        this.scripts = scripts;
        this.wait = wait;
    }

    public EnemyScript get(int index) {
        return scripts.get(index);
    }

    public int size() {
        return scripts.size();
    }

    public List<EnemyScript> scripts() {
        return scripts;
    }

    public int waitAfterEnd() {
        return wait;
    }
}
