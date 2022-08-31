package fr.poulpogaz.jam.stage;

public final class EnemyAction<P> {

    private final int start;
    private final P pattern;

    public EnemyAction(int start, P pattern) {
        this.start = start;
        this.pattern = pattern;
    }

    public int start() {
        return start;
    }

    public P pattern() {
        return pattern;
    }
}
