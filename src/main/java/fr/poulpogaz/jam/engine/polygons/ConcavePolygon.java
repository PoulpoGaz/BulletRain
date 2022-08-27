package fr.poulpogaz.jam.engine.polygons;

import fr.poulpogaz.jam.engine.Report;
import org.joml.Vector2f;

public class ConcavePolygon extends MultiplePointPolygon {

    public ConcavePolygon() {
        super();
    }

    @Override
    protected boolean checkValid(Vector2f point) {
        return true;
    }

    @Override
    public Report collide(Polygon polygon) {
        if (polygon.isAABB()) {
            return engine.aabbConcave((AABB) polygon, this);

        } else if (polygon.isCircle()) {
            return engine.circleConcave((Circle) polygon, this);

        } else if (polygon.isConvex()) {
            return engine.convexConcave((ConvexPolygon) polygon, this);

        } else if (polygon.isConcave()) {
            return engine.concaveConcave(this, (ConcavePolygon) polygon);
        } else {
            return engine.concavePolygon(this, polygon);
        }
    }

    @Override
    public boolean isConcave() {
        return true;
    }

    @Override
    public boolean isConvex() {
        return false;
    }
}
