package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollideHelper {

    public static boolean aabb(AABB a, HitBox b) {
        if (b instanceof AABB aabb) {
            return aabbAABB(a, aabb);
        } else if (b instanceof Circle c) {
            return aabbCircle(a, c);
        } else if (b instanceof Polygon p) {
            return aabbPolygon(a, p);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean circle(Circle c, HitBox b) {
        if (b instanceof AABB aabb) {
            return aabbCircle(aabb, c);
        } else if (b instanceof Circle c2) {
            return circleCircle(c, c2);
        } else if (b instanceof Polygon p) {
            return circlePolygon(c, p);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean polygon(Polygon a, HitBox b) {
        if (b instanceof AABB aabb) {
            return aabbPolygon(aabb, a);
        } else if (b instanceof Circle c) {
            return circlePolygon(c, a);
        } else if (b instanceof Polygon r) {
            return polygonPolygon(a, r);
        } else {
            throw new UnsupportedOperationException();
        }
    }



    public static boolean aabbAABB(AABB a, AABB b) {
        float x0 = a.getX();
        float y0 = a.getY();
        float w0 = a.getWidth();
        float h0 = a.getHeight();

        float x1 = b.getX();
        float y1 = b.getY();
        float w1 = b.getWidth();
        float h1 = b.getHeight();

        return x0 + w0 > x1 &&
                y0 + h0 > y1 &&
                x0 < x1 + w1 &&
                y0 < y1 + h1;
    }

    public static boolean aabbCircle(AABB a, Circle c) {
        float x1 = a.getX();
        float y1 = a.getY();

        float x2 = a.getX() + a.getWidth();
        float y2 = a.getY() + a.getHeight();

        return circlePoint(c, x1, y1) || circlePoint(c, x2, y1) ||
                circlePoint(c, x2, y2) || circlePoint(c, x1, y2) ||
                circleEdge(c, x1, y1, x2, y1) ||
                circleEdge(c, x2, y1, x2, y2) ||
                circleEdge(c, x2, y2, x1, y2) ||
                circleEdge(c, x1, y2, x1, y1);
    }

    public static boolean aabbPolygon(AABB a, Polygon b) {
        // TODO: avoid creation of arraylist + vectors
        List<Vector2> v = new ArrayList<>(4);

        float x1 = a.getX();
        float y1 = a.getY();

        float x2 = a.getX() + a.getWidth();
        float y2 = a.getY() + a.getHeight();

        v.add(new Vector2(x1, y1));
        v.add(new Vector2(x2, y1));
        v.add(new Vector2(x2, y2));
        v.add(new Vector2(x1, y2));

        return sat(v, b.getPoints()) || sat(b.getPoints(), v);
    }

    public static boolean circleCircle(Circle a, Circle b) {
        float d = a.getCenter().dst2(b.getCenter());

        float totRad = a.getRadius() + b.getRadius();

        return d <= totRad * totRad;
    }

    public static boolean circlePolygon(Circle c, Polygon b) {
        List<Vector2> points = b.getPoints();

        for (int i = 0; i < points.size(); i++) {
            Vector2 p = points.get(i);
            Vector2 p2 = points.get((i + 1) % points.size());

            if (circleEdge(c, p.x, p.y, p2.x, p2.y) || circlePoint(c, p.x, p.y)) {
                return true;
            }
        }

        return false;
    }




    public static boolean polygonPolygon(Polygon a, Polygon b) {
        return sat(a.getPoints(), b.getPoints()) ||
                sat(b.getPoints(), a.getPoints());
    }

    private static boolean sat(List<Vector2> pointsA, List<Vector2> pointsB) {
        Vector2 proj = new Vector2();

        for (int i = 0; i < pointsA.size(); i++) {
            Vector2 a1 = pointsA.get(i);
            Vector2 a2 = pointsA.get((i + 1) % pointsA.size());

            proj.set(-(a2.y - a1.y), a2.x - a1.x);

            float min_a = Float.MAX_VALUE;
            float max_a = Float.MIN_VALUE;

            for (Vector2 p : pointsA) {
                float q = p.dot(proj);

                min_a = Math.min(min_a, q);
                max_a = Math.max(max_a, q);
            }

            float min_b = Float.MAX_VALUE;
            float max_b = Float.MIN_VALUE;

            for (Vector2 p : pointsB) {
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


    public static boolean circleEdge(Circle c, float x1, float y1, float x2, float y2) {
        Vector2 d = new Vector2(x2, y2).sub(x1, y1);
        Vector2 e = new Vector2(x1, y1).sub(c.getCenter());

        float A = d.dot(d);
        float B = 2 * d.dot(e);
        float C = e.dot(e) - c.getRadiusSquared();

        float discriminant = B * B - 4 * A * C;

        return discriminant >= 0;
    }

    public static boolean circlePoint(Circle c, float x, float y) {
        return c.getCenter().dst2(x, y) <= c.getRadiusSquared();
    }
}
