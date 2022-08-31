package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements HitBox {

    private final List<Vector2> points;
    private final List<Vector2> models;
    private Vector2 center;

    public Polygon() {
        points = new ArrayList<>();
        models = new ArrayList<>();
        center = new Vector2();
    }

    public Polygon(Vector2 center) {
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
            Vector2 v = points.get(i);
            models.get(i).cpy().add(center);
        }
    }

    @Override
    public void getAABB(AABB dest) {
        float min_x = Integer.MAX_VALUE;
        float max_x = Integer.MIN_VALUE;
        float min_y = Integer.MAX_VALUE;
        float max_y = Integer.MIN_VALUE;

        for (Vector2 point : points) {
            min_x = Math.min(min_x, point.x);
            max_x = Math.max(max_x, point.x);

            min_y = Math.min(min_y, point.y);
            max_y = Math.max(max_y, point.y);
        }

        dest.set(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    public void addPoint(Vector2 point) {
        models.add(point);
        points.add(point.cpy().add(center));
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public Vector2 getCenter() {
        return center;
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public Vector2 getPoint(int index) {
        return points.get(index);
    }

    public List<Vector2> getModels() {
        return models;
    }

    public Vector2 getModel(int index) {
        return models.get(index);
    }
}
