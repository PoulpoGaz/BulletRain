package fr.poulpogaz.jam.engine.polygons;

import org.joml.Vector2f;

public class Edge extends AbstractPolygon {

    private Vector2f start;
    private Vector2f end;

    public Edge() {
        start = new Vector2f();
        end = new Vector2f();
    }

    public Edge(float x0, float y0, float x1, float y1) {
        start = new Vector2f(x0, y0);
        end = new Vector2f(x1, y1);
    }

    public Edge(Vector2f start, Vector2f end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean collide(Polygon polygon) {
        if (polygon.isEdge()) {
            return engine.edgeEdge(this, (Edge) polygon);

        } else if (polygon.isPoint()) {
            return engine.pointEdge((Vector2f) polygon, this);

        } else if (polygon.isAABB()) {
            return engine.edgeAABB(this, (AABB) polygon);

        } else if (polygon.isCircle()) {
            return engine.edgeCircle(this, (Circle) polygon);

        } else if (polygon.isConvex()) {
            return engine.edgeConvex(this, (ConvexPolygon) polygon);

        } else if (polygon.isConcave()) {
            return engine.edgeConcave(this, (ConcavePolygon) polygon);

        } else {
            return engine.edgePolygon(this, polygon);
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
        return true;
    }

    @Override
    public boolean isPoint() {
        return false;
    }

    @Override
    public AABB getAABB() {
        float x = Math.min(start.x, end.x);
        float y = Math.min(start.y, end.y);
        float xw = Math.max(start.x, end.x);
        float yh = Math.max(start.y, end.y);

        return new AABB(x, y, xw - x, yh - y);
    }

    @Override
    public Vector2f[] models() {
        return null;
    }

    @Override
    public Vector2f[] points() {
        return new Vector2f[]{start, end};
    }

    @Override
    public Vector2f center() {
        return new Vector2f((start.x + end.x) / 2, (start.y + end.y) / 2);
    }

    @Override
    public void setCenter(Vector2f center) {

    }

    public Vector2f getStart() {
        return start;
    }

    public void setStart(Vector2f start) {
        this.start = start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public void setEnd(Vector2f end) {
        this.end = end;
    }
}