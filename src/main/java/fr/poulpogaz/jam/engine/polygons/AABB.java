package fr.poulpogaz.jam.engine.polygons;

import fr.poulpogaz.jam.engine.Report;
import org.joml.Vector2f;

public class AABB extends AbstractPolygon {

    private float x;
    private float y;
    private float width;
    private float height;

    public AABB() {
        this(0, 0, 0, 0);
    }

    public AABB(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Report collide(Polygon polygon) {
        if (polygon.isPoint()) {
            return engine.pointAABB(polygon.center(), this);

        } else if (polygon.isEdge()) {
            return engine.edgeAABB((Edge) polygon, this);

        } else if (polygon.isAABB()) {
            return engine.aabbAABB(this, polygon.getAABB());

        } else if (polygon.isCircle()) {
            return engine.aabbCircle(this, (Circle) polygon);

        } else if (polygon.isConvex()) {
            return engine.aabbConvex(this, (ConvexPolygon) polygon);

        } else if (polygon.isConcave()) {
            return engine.aabbConcave(this, (ConcavePolygon) polygon);
        } else {
            return engine.aabbPolygon(this, polygon);
        }
    }

    @Override
    public boolean isCircle() {
        return false;
    }

    @Override
    public boolean isConcave() {
        return false;
    }

    @Override
    public boolean isConvex() {
        return true;
    }

    @Override
    public boolean isAABB() {
        return true;
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
        return this;
    }

    @Override
    public Vector2f[] models() {
        Vector2f[] models = new Vector2f[4];

        float halfW = width / 2;
        float halfH = width / 2;

        models[0] = new Vector2f(-halfW, -halfH);
        models[1] = new Vector2f(+halfW, -halfH);
        models[2] = new Vector2f(+halfW, +halfH);
        models[3] = new Vector2f(+halfW, -halfH);

        return models;
    }

    @Override
    public Vector2f[] points() {
        Vector2f[] points = new Vector2f[4];

        float xw = x + width;
        float yh = y + height;

        points[0] = new Vector2f(x,  y);
        points[1] = new Vector2f(xw, y);
        points[2] = new Vector2f(xw, yh);
        points[3] = new Vector2f(x,  yh);

        return points;
    }

    @Override
    public Vector2f center() {
        return new Vector2f(x + width / 2, y + height / 2);
    }

    @Override
    public void setCenter(Vector2f center) {
        x = center.x - width / 2;
        y = center.y - height / 2;
    }

    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void set(Vector2f a, Vector2f b) {
        this.x = Math.min(a.x, b.x);
        this.y = Math.min(a.y, b.y);
        this.width = Math.abs(a.x - b.x);
        this.height = Math.abs(a.y - b.y);
    }

    @Override
    public String toString() {
        return "AABB [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", center=" + center() + "]";
    }
}