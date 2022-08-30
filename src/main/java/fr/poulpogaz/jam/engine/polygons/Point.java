package fr.poulpogaz.jam.engine.polygons;

import org.joml.Vector2f;

import java.util.Objects;

public class Point extends AbstractPolygon {

    private Vector2f point;

    public Point(Vector2f point) {
        this.point = Objects.requireNonNullElseGet(point, Vector2f::new);
    }


    @Override
    public boolean collide(Polygon polygon) {
        if (polygon.isPoint()) {
            return engine.pointPoint(this, (Point) polygon);

        } else if (polygon.isEdge()) {
            return engine.pointEdge(this, (Edge) polygon);

        } else if (polygon.isAABB()) {
            return engine.pointAABB(this, polygon.getAABB());

        } else if (polygon.isCircle()) {
            return engine.pointCircle(this, (Circle) polygon);

        } else if (polygon.isConvex()) {
            return engine.pointConvex(this, (ConvexPolygon) polygon);

        } else if (polygon.isConcave()) {
            return engine.pointConcave(this, (ConcavePolygon) polygon);
        } else {
            return engine.pointPolygon(this, polygon);
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
        return true;
    }

    @Override
    public AABB getAABB() {
        return new AABB(point.x, point.y, 0, 0);
    }

    @Override
    public Vector2f[] models() {
        return new Vector2f[] {new Vector2f()};
    }

    @Override
    public Vector2f[] points() {
        return new Vector2f[] {point};
    }

    @Override
    public Vector2f center() {
        return point;
    }

    @Override
    public void setCenter(Vector2f center) {
        point.set(center);
    }

    public float x() {
        return point.x;
    }

    public float y() {
        return point.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point1 = (Point) o;

        return point.equals(point1.point);
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }
}
