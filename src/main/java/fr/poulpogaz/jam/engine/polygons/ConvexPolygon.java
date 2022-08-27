package fr.poulpogaz.jam.engine.polygons;

import fr.poulpogaz.jam.engine.Report;
import org.joml.Vector2f;

import java.util.ArrayList;

public class ConvexPolygon extends MultiplePointPolygon {

    public ConvexPolygon() {
        super();
    }

    @Override
    protected boolean checkValid(Vector2f point) {
        if (models.size() > 2) {
            models.add(point);

            ArrayList<Double> angles = new ArrayList<>(models.size());

            for (int i = -1; i < models.size() - 1; i++) {
                Vector2f a = models.get((i + 1) % models.size());
                Vector2f b = models.get((i + 2) % models.size());
                Vector2f c = models.get((i + 3) % models.size());

                Vector2f ab = (Vector2f) b.sub(a, new Vector2f());
                Vector2f bc = (Vector2f) b.sub(c, new Vector2f());

                float numerator = ab.dot(bc);
                float denominator = ab.length() * bc.length();

                float angle = (float) Math.acos(numerator / denominator);

                angles.add(Math.toDegrees(angle));
            }

            models.remove(point);

            double sumAngle = angles.stream().mapToDouble((a) -> a).sum();

            return Math.round(sumAngle) == (models.size() - 1) * 180;
        }

        return true;
    }

    @Override
    public Report collide(Polygon polygon) {
        if (polygon.isAABB()) {
            return engine.aabbConvex((AABB) polygon, this);

        } else if (polygon.isCircle()) {
            return engine.circleConvex((Circle) polygon, this);

        } else if (polygon.isConvex()) {
            return engine.convexConvex(this, (ConvexPolygon) polygon);

        } else if (polygon.isConcave()) {
            return engine.convexConcave(this, (ConcavePolygon) polygon);
        } else {
            return engine.convexPolygon(this, polygon);
        }
    }

    @Override
    public boolean isConcave() {
        return false;
    }

    @Override
    public boolean isConvex() {
        return true;
    }
}