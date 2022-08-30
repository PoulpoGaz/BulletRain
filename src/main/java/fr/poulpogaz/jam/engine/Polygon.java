package fr.poulpogaz.jam.engine;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements HitBox {

    private final List<Vector2f> points;
    private final List<Vector2f> models;
    private Vector2f center;

    public Polygon() {
        points = new ArrayList<>();
        models = new ArrayList<>();
        center = new Vector2f();
    }

    public Polygon(Vector2f center) {
        this.center = center;
        points = new ArrayList<>();
        models = new ArrayList<>();
    }

    @Override
    public boolean collide(HitBox b) {
        return CollideHelper.polygon(this, b);
    }

    public void reloadPoints() {
        for (int i = 0; i < points.size(); i++) {
            Vector2f v = points.get(i);
            models.get(i).add(center, v);
        }
    }

    @Override
    public void getAABB(AABB dest) {
        float min_x = Integer.MAX_VALUE;
        float max_x = Integer.MIN_VALUE;
        float min_y = Integer.MAX_VALUE;
        float max_y = Integer.MIN_VALUE;

        for (Vector2f point : points) {
            min_x = Math.min(min_x, point.x);
            max_x = Math.max(max_x, point.x);

            min_y = Math.min(min_y, point.y);
            max_y = Math.max(max_y, point.y);
        }

        dest.set(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    public void addPoint(Vector2f point) {
        models.add(point);
        points.add(point.add(center, new Vector2f()));
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public Vector2f getCenter() {
        return center;
    }

    public List<Vector2f> getPoints() {
        return points;
    }

    public Vector2f getPoint(int index) {
        return points.get(index);
    }

    public List<Vector2f> getModels() {
        return models;
    }

    public Vector2f getModel(int index) {
        return models.get(index);
    }
}
