package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.*;
import org.joml.Vector2f;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DCE implements ICollideEngine {

    private static final DCE INSTANCE = new DCE();

    public static DCE getInstance() {
        return INSTANCE;
    }

    private ArrayList<Report> reports;
    private boolean calculateIntersection = false;

    private DCE() {
        reports = new ArrayList<>();
    }

    // START OF IMPLEMENTS METHOD

    // POINT

    @Override
    public Report pointPoint(Point p1, Point p2) {
        if (p1.equals(p2)) {
            Report report = trueReport(p1, p2);

            if (calculateIntersection) {
                report = trueReport(p1, p2);
                report.addPoint(p1.center());
            }

            return report;
        }

        return falseReport(p1, p2);
    }

    @Override
    public Report pointEdge(Point p, Edge e) {
        Vector2f s = e.getStart();
        Vector2f en = e.getEnd();

        float x0 = min(s.x, en.x);
        float y0 = min(s.y, en.y);
        float x1 = max(s.x, en.x);
        float y1 = max(s.y, en.y);

        if (between(p.x(), x0, x1) && between(p.y(), y0, y1)) {

            float a  = (y0 - y1) / (x0 - x1);
            float a2 = (p.y() - y1) / (p.x() - x1);

            if (a == a2) {

                Report report = trueReport(p, e);

                if (calculateIntersection) {
                    report.addPoint(p.center());

                }
                return report;
            }
        }

        return falseReport(p, e);
    }

    @Override
    public Report pointAABB(Point p, AABB a) {
        float x0 = a.getX();
        float y0 = a.getY();
        float x1 = x0 + a.getWidth();
        float y1 = y0 + a.getHeight();

        if (between(p.x(), x0, x1) && between(p.x(), y0, y1)) {

            Report report = trueReport(p, a);
            if (calculateIntersection) {

                if (p.x() == x0 || p.x() == x1 || p.y() == y0 || p.y() == y1) {

                    report.addPoint(p.center());
                }
            }

            return report;
        }

        return falseReport(p, a);
    }

    @Override
    public Report pointCircle(Point p, Circle c) {
        float dist = p.center().distanceSquared(c.center());

        float rad2 = c.getRadius() * c.getRadius();

        if (dist <= rad2) { // p is in c

            Report report = trueReport(p, c);

            if (calculateIntersection && dist == rad2) { // p intersect c
                report = trueReport(p, c);
                report.addPoint(p.center());
            }

            return report;
        }

        return falseReport(p, c);
    }

    @Override
    public Report pointConvex(Point p, ConvexPolygon c) {
        return pointPolygon(p, c);
    }

    @Override
    public Report pointConcave(Point p, ConcavePolygon c) {
        return pointPolygon(p, c);
    }

    // http://www.alienryderflex.com/polygon/
    @Override
    public Report pointPolygon(Point p, Polygon polygon) {
        boolean intersect = false;

        Vector2f[] points = polygon.points();

        for (int i = 0; i < points.length; i++) {
            Vector2f a = points[i];
            Vector2f b = points[(i + 1) % points.length];

            if (((a.y < p.y() && b.y >= p.y())
                    || b.y < p.y() && a.y >= p.y())
                    && (a.x <= p.x() || b.x <= p.x())) {

                if (a.x + (p.y() - a.y) / (b.y - a.y) * (b.x - a.x) < p.x()) {
                    intersect = !intersect;
                }
            }
        }

        return new Report(intersect, p, polygon);
    }


    // EDGE

    @Override
    public Report edgeEdge(Edge e1, Edge e2) {
        Vector2f a = e1.getStart();
        Vector2f b = e1.getEnd();
        Vector2f c = e2.getStart();
        Vector2f d = e2.getEnd();

        float x1x3 = a.x - c.x;
        float y1y3 = a.y - c.y;

        float x3x4 = c.x - d.x;
        float y3y4 = c.y - d.y;

        float x1x2 = a.x - b.x;
        float y1y2 = a.y - b.y;

        float denominator = x1x2 * y3y4 - y1y2 * x3x4;

        if (denominator != 0) {
            float t = (x1x3 * y3y4 - y1y3 * x3x4) / denominator;
            float u = -(x1x2 * y1y3 - y1y2 * x1x3) / denominator;

            if (between(t, 0, 1) && between(u, 0, 1)) {
                Report report = trueReport(e1, e2);

                if (calculateIntersection) {

                    report.addPoint(new Vector2f(a.x + t * (b.x - a.x), a.y + t * (b.y - a.y)));
                }

                return report;
            }
        }

        return falseReport(e1, e2);
    }

    // https://tavianator.com/fast-branchless-raybounding-box-intersections/
    @Override
    public Report edgeAABB(Edge e, AABB a) {
        return polygonPolygon(e, a);
    }

    @Override
    public Report edgeCircle(Edge e, Circle c) {
        return circlePolygon(c, e);
    }

    @Override
    public Report edgeConvex(Edge e, ConvexPolygon c) {
        return convexPolygon(c, e);
    }

    @Override
    public Report edgeConcave(Edge e, ConcavePolygon c) {
        return polygonPolygon(e, c);
    }

    @Override
    public Report edgePolygon(Edge e, Polygon p) {
        return falseReport(e, p);
    }


    // AABB

    @Override
    public Report aabbAABB(AABB a, AABB b) {
        if (a.isEmpty() || b.isEmpty()) {
            return falseReport(a, b);
        }

        float x0 = a.getX();
        float y0 = a.getY();
        float w0 = a.getWidth();
        float h0 = a.getHeight();

        float x1 = b.getX();
        float y1 = b.getY();
        float w1 = b.getWidth();
        float h1 = b.getHeight();

        if ((x0 + w0 > x1 &&
                y0 + h0 > y1 &&
                x0 < x1 + w1 &&
                y0 < y1 + h1)) {

            Report report = trueReport(a, b);

            if (calculateIntersection) {
                Vector2f[] p1 = a.points();
                Vector2f[] p2 = b.points();

                for (int i = 0; i < p1.length; i++) {
                    Vector2f p1s = p1[i];
                    Vector2f p1e = p1[(i + 1) % p1.length];

                    for (int j = 0; j < p2.length; j++) {

                        Vector2f p2s = p2[j];
                        Vector2f p2e = p2[(j + 1) % p2.length];

                        Report report2 = edgeEdge(new Edge(p1s, p1e), new Edge(p2s, p2e));

                        if (report2.isIntersect()) {
                            report.combine(report2);
                        }
                    }
                }
            }

            return report;
        }

        return falseReport(a, b);
    }

    @Override
    public Report aabbCircle(AABB a, Circle c) {
        return circlePolygon(c, a);
    }

    @Override
    public Report aabbConvex(AABB a, ConvexPolygon p) {
        return convexPolygon(p, a);
    }

    @Override
    public Report aabbConcave(AABB a, ConcavePolygon p) {
        return polygonPolygon(a, p);
    }

    @Override
    public Report aabbPolygon(AABB a, Polygon p) {
        return polygonPolygon(a, p);
    }


    // CIRCLE

    // http://paulbourke.net/geometry/circlesphere/
    @Override
    public Report circleCircle(Circle c0, Circle c1) {
        float d = c1.center().distanceSquared(c0.center());

        float totRad = c0.getRadius() + c1.getRadius();

        if (d <= totRad * totRad) {

            Report report = trueReport(c0, c1);

            if (calculateIntersection) {

                Vector2f p0 = c0.center();
                Vector2f p1 = c1.center();

                float r0 = c0.getRadius();
                float r1 = c1.getRadius();

                d = (float) Math.sqrt(d);

                if (!((d > d) ||
                        (d < Math.abs(r0 - r1)) ||
                        (d == 0 && r0 == r1))) {

                    float rad0sq = r0 * r0;

                    float a = (rad0sq - r1 * r1 + d * d) / (2 * d);

                    float h = (float) Math.sqrt(rad0sq - a * a);

                    Vector2f dir = (Vector2f) p1.sub(p0, new Vector2f());

                    Vector2f p2 = (Vector2f) dir.mul(a / d, new Vector2f()).add(p0);

                    float x3 = p2.x + h * dir.y / d;
                    float y3 = p2.y - h * dir.x / d;

                    float x4 = p2.x - h * dir.y / d;
                    float y4 = p2.y + h * dir.x / d;

                    report.addPoint(new Vector2f(x3, y3));
                    report.addPoint(new Vector2f(x4, y4));
                }
            }

            return report;
        }

        return falseReport(c0, c1);
    }

    @Override
    public Report circleConvex(Circle c, ConvexPolygon convex) {
        return circlePolygon(c, convex);
    }

    @Override
    public Report circleConcave(Circle c, ConcavePolygon concave) {
        return circlePolygon(c, concave);
    }

    // https://stackoverflow.com/questions/1073336/circle-line-segment-collision-detection-algorithm
    @Override
    public Report circlePolygon(Circle c, Polygon p) {
        if (c.isEmpty()) {
            return falseReport(c, p);
        }

        Vector2f[] points = p.points();

        Vector2f center = c.center();
        float r2 = c.getRadius() * c.getRadius();

        Report report = null;
        boolean intersect = false;
        for (int i = 0; i < points.length; i++) {
            Vector2f p1 = points[i];
            Vector2f p2 = points[(i + 1) % points.length];

            Vector2f d = (Vector2f) p2.sub(p1, new Vector2f());
            Vector2f e = (Vector2f) p1.sub(center, new Vector2f());

            float A = d.dot(d);
            float B = 2 * d.dot(e);
            float C = e.dot(e) - r2;

            float discriminant = B * B - 4 * A * C;

            if (discriminant >= 0) {
                float sqrt = (float) Math.sqrt(discriminant);

                float t1 = (-B - sqrt) / (2 * A);
                float t2 = (-B + sqrt) / (2 * A);

                if (between(t1, 0, 1) || between(t2, 0, 1)) {

                    if (report == null) {
                        report = trueReport(c, p);
                    }

                    if (calculateIntersection) {
                        if (between(t1, 0, 1)) {
                            report.addPoint(new Vector2f(p1.x + t1 * d.x, p1.y + t1 * d.y));
                        }

                        if (between(t2, 0, 1)) {
                            report.addPoint(new Vector2f(p1.x + t2 * d.x, p1.y + t2 * d.y));
                        }

                        intersect = true;
                        continue;
                    }

                    return report;
                }
            }
        }

        if (intersect) {
            return report;
        } else {

            for (Vector2f point: points) {

                if (pointCircle(point, c).isIntersect()) {
                    return trueReport(c, p);
                }
            }
        }

        return falseReport(c, p);
    }


    // CONVEX

    @Override
    public Report convexConvex(ConvexPolygon c1, ConvexPolygon c2) {
        return convexPolygon(c1, c2);
    }

    @Override
    public Report convexConcave(ConvexPolygon c1, ConcavePolygon c2) {
        return polygonPolygon(c1, c2);
    }

    @Override
    public Report convexPolygon(ConvexPolygon c, Polygon p) {
        if (p.isConvex() && !calculateIntersection) {
            Vector2f[] points1 = c.points();
            Vector2f[] points2 = p.points();

            if (!sat(points1, points2)) {
                return falseReport(c, p);
            }

            return new Report(sat(points2, points1), c, p);
        } else {
            return polygonPolygon(c, p);
        }
    }

    private boolean sat(Vector2f[] pointsA, Vector2f[] pointsB) {
        for (int i = 0; i < pointsA.length; i++) {
            Vector2f a1 = pointsA[i];
            Vector2f a2 = pointsA[(i + 1) % pointsA.length];

            Vector2f proj = new Vector2f(-(a2.y - a1.y), a2.x - a1.x);

            float min_a = Float.MAX_VALUE;
            float max_a = Float.MIN_VALUE;

            for (Vector2f p: pointsA) {
                float q = p.dot(proj);

                min_a = Math.min(min_a, q);
                max_a = Math.max(max_a, q);
            }

            float min_b = Float.MAX_VALUE;
            float max_b = Float.MIN_VALUE;

            for (Vector2f p: pointsB) {
                float q = p.dot(proj);

                min_b = Math.min(min_b, q);
                max_b = Math.max(max_b, q);
            }

            if (!(max_b >= min_a && max_a >= min_b)) {
                return false;
            }
        }

        return true;
    }


    // CONCAVE

    @Override
    public Report concaveConcave(ConcavePolygon c1, ConcavePolygon c2) {
        return polygonPolygon(c1, c2);
    }

    @Override
    public Report concavePolygon(ConcavePolygon c, Polygon p) {
        return polygonPolygon(c, p);
    }

    @Override
    public Report polygonPolygon(Polygon p1, Polygon p2) {
        Vector2f[] points1 = p1.points();
        Vector2f[] points2 = p2.points();

        Report report = null;
        boolean intersect = false;
        for (int i = 0; i < points1.length; i++) {
            Vector2f p = points1[i];
            Vector2f p_r = points1[(i + 1) % points1.length];

            for (int j = 0; j < points2.length; j++) {

                Vector2f q = points2[j];
                Vector2f q_s = points2[(j + 1) % points2.length];

                Report lineLineReport = edgeEdge(new Edge(p, p_r), new Edge(q, q_s));

                if (lineLineReport.isIntersect()) {
                    if (report == null) {
                        report = trueReport(p1, p2);
                    }

                    if (calculateIntersection) {
                        report.combine(lineLineReport);
                        intersect = true;
                        continue;
                    }

                    return report;
                }
            }
        }

        if (intersect) {
            return report;
        }

        for (Vector2f point: p1.points()) {
            if (pointPolygon(point, p2).isIntersect()) {
                return trueReport(p1, p2);
            }
        }

        return falseReport(p1, p2);
    }

    // END OF IMPLEMENTS METHOD

    // START OF UTILS METHOD

    private boolean between(float value, float a, float b) {
        float min = min(a, b);
        float max = max(a, b);

        return min <= value && value <= max;
    }

    // END OF UTILS METHOD

    // START OF REPORT PART

    @Override
    public void calculateIntersection(boolean intersection) {
        calculateIntersection = intersection;
    }

    @Override
    public boolean isCalculateIntersection() {
        return calculateIntersection;
    }

    private Report trueReport(Polygon first, Polygon second) {
        Report report = new Report(true, first, second);

        reports.add(report);

        return report;
    }

    private Report falseReport(Polygon first, Polygon second) {
        Report report = new Report(false, first, second);

        reports.add(report);

        return report;
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void clearReports() {
        reports.clear();
    }

    // END OF REPORT PART
}