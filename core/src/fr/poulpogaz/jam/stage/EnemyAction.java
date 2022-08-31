package fr.poulpogaz.jam.stage;

public final class EnemyAction<P> {

    private final float start;
    private final P pattern;

    public EnemyAction(float start, P pattern) {
        this.start = start;
        this.pattern = pattern;
    }

    public float start() {
        return start;
    }

    public P pattern() {
        return pattern;
    }
}
