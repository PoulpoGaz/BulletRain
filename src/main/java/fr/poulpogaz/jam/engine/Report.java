package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.Polygon;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private final boolean intersect;

    private final Polygon first;
    private final Polygon second;
    private final List<Vector2f> intersectPoints;

    public Report(boolean intersect, Polygon first, Polygon second) {
        this.intersect = intersect;
        this.first = first;
        this.second = second;
        this.intersectPoints = new ArrayList<>();
    }

    void addPoint(Vector2f point) {
        intersectPoints.add(point);
    }

    void combine(Report report) {
        intersectPoints.addAll(report.getIntersectPoints());
    }

    public boolean intersect() {
        return intersect;
    }

    public Polygon getFirst() {
        return first;
    }

    public Polygon getSecond() {
        return second;
    }

    public List<Vector2f> getIntersectPoints() {
        return intersectPoints;
    }

}