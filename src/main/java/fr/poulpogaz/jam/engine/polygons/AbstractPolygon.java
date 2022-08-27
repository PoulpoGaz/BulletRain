package fr.poulpogaz.jam.engine.polygons;

import fr.poulpogaz.jam.engine.DCE;
import fr.poulpogaz.jam.engine.ICollideEngine;

public abstract class AbstractPolygon implements Polygon {

    protected ICollideEngine engine = DCE.getInstance();

    @Override
    public ICollideEngine getCollideEngine() {
        return engine;
    }

    @Override
    public void setCollideEngine(ICollideEngine engine) {
        this.engine = engine;
    }
}