package fr.poulpogaz.jam.stage;

public abstract class BaseBuilder {

    protected final StageBuilder parent;

    public BaseBuilder(StageBuilder parent) {
        this.parent = parent;
    }

    public abstract StageBuilder build();
}
