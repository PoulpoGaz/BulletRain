package fr.poulpogaz.jam.engine.polygons;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class MultiplePointPolygon extends AbstractPolygon{

    protected Vector2f center;
    protected ArrayList<Vector2f> models;
    protected ArrayList<Vector2f> points;

    public MultiplePointPolygon() {
        center = new Vector2f();
        models = new ArrayList<>();
        points = new ArrayList<>();
    }

    public void addPoint(Vector2f point) {
        if (checkValid(point)) {
            models.add(point);

            reloadPoints();
        }
    }

    public void removePoint(Vector2f point) {
        models.remove(point);

        reloadPoints();
    }

    public void removePoint(int index) {
        models.remove(index);

        reloadPoints();
    }

    public void reset() {
        models.clear();
        points.clear();
    }

    protected abstract boolean checkValid(Vector2f point);

    public void reloadPoints() {
        reloadPoints((vec) -> vec.add(center, new Vector2f()));
    }

    public void reloadPoints(Function<Vector2f, Vector2f> function) {
        points.clear();

        for (Vector2f model : models) {
            points.add(function.apply(model));
        }
    }

    @Override
    public boolean isCircle() {
        return false;
    }

    @Override
    public boolean isAABB() {
        return false;
    }

    @Override
    public boolean isEdge() {
        return false;
    }

    @Override
    public boolean isPoint() {
        return false;
    }

    @Override
    public AABB getAABB() {
        float min_x = Integer.MAX_VALUE;
        float max_x = Integer.MIN_VALUE;
        float min_y = Integer.MAX_VALUE;
        float max_y = Integer.MIN_VALUE;

        for (Vector2f point: points) {
            min_x = Math.min(min_x, point.x);
            max_x = Math.max(max_x, point.x);

            min_y = Math.min(min_y, point.y);
            max_y = Math.max(max_y, point.y);
        }

        return new AABB(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    @Override
    public Vector2f[] models() {
        return models.toArray(new Vector2f[0]);
    }

    @Override
    public Vector2f[] points() {
        return points.toArray(new Vector2f[0]);
    }

    @Override
    public Vector2f center() {
        return center;
    }

    @Override
    public void setCenter(Vector2f center) {
        this.center = center;
        reloadPoints();
    }
}