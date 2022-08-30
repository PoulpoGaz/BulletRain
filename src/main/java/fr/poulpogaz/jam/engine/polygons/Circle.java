package fr.poulpogaz.jam.engine.polygons;

import org.joml.Vector2f;

public class Circle extends AbstractPolygon {

    private Vector2f center;
    private float radius;

    public Circle() {
        center = new Vector2f();
        radius = 0;
    }

    public Circle(float radius) {
        this.radius = radius;
        center = new Vector2f();
    }

    public Circle(float radius, Vector2f center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public boolean collide(Polygon polygon) {
        if (polygon.isAABB()) {
            return engine.aabbCircle((AABB) polygon, this);
        } else if (polygon.isCircle()) {
            return engine.circleCircle(this, (Circle) polygon);
        } else if (polygon.isConvex()) {
            return engine.circleConvex(this, (ConvexPolygon) polygon);
        } else if (polygon.isConcave()) {
            return engine.circleConcave(this, (ConcavePolygon) polygon);
        } else {
            return engine.circlePolygon(this, polygon);
        }
    }

    @Override
    public boolean isCircle() {
        return true;
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
        return new AABB(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }

    @Override
    public Vector2f[] models() {
        return null;
    }

    @Override
    public Vector2f[] points() {
        return null;
    }

    @Override
    public Vector2f center() {
        return center;
    }

    @Override
    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public boolean isEmpty() {
        return radius <= 0;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}