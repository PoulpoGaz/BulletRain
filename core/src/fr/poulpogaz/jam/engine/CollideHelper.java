package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollideHelper {

    public static boolean aabb(AABB a, HitBox b) {
        if (b instanceof AABB) {
            return aabbAABB(a, (AABB) b);
        } else if (b instanceof Circle) {
            return aabbCircle(a, (Circle) b);
        } else if (b instanceof Polygon) {
            return aabbPolygon(a, (Polygon) b);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean circle(Circle c, HitBox b) {
        if (b instanceof AABB) {
            return aabbCircle((AABB) b, c);
        } else if (b instanceof Circle) {
            return circleCircle(c, (Circle) b);
        } else if (b instanceof Polygon) {
            return circlePolygon(c, (Polygon) b);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean polygon(Polygon a, HitBox b) {
        if (b instanceof AABB) {
            return aabbPolygon((AABB) b, a);
        } else if (b instanceof Circle) {
            return circlePolygon((Circle) b, a);
        } else if (b instanceof Polygon ) {
            return polygonPolygon(a, (Polygon) b);
        } else {
            throw new UnsupportedOperationException();
        }
    }



    public static boolean aabbAABB(AABB a, AABB b) {
        float tw = a.getWidth();
        float th = a.getHeight();
        float rw = b.getWidth();
        float rh = b.getHeight();
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        float tx = a.getX();
        float ty = a.getY();
        float rx = b.getX();
        float ry = b.getY();
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
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
                circleEdge(c, x1, y2, x1, y1) ||
                aabbPoint(a, c.getCenter().x, c.getCenter().y);
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

        return polygonPoint(b, c.getCenter().x, c.getCenter().y);
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

        if (discriminant >= 0) {
            float sqrt = (float) Math.sqrt(discriminant);

            float t1 = (-B - sqrt) / (2 * A);
            float t2 = (-B + sqrt) / (2 * A);

            return between(t1, 0, 1) || between(t2, 0, 1);
        }

        return false;
    }

    public static boolean circlePoint(Circle c, float x, float y) {
        return c.getCenter().dst2(x, y) <= c.getRadiusSquared();
    }

    public static boolean polygonPoint(Polygon p, float x, float y) {
        List<Vector2> points = p.getPoints();

        boolean intersect = false;
        for (int i = 0; i < points.size(); i++) {
            Vector2 a = points.get(i);
            Vector2 b = points.get((i + 1) % points.size());

            if (((a.y < y && b.y >= y)
                    || b.y < y && a.y >= y)
                    && (a.x <= x || b.x <= x)) {

                if (a.x + (y - a.y) / (b.y - a.y) * (b.x - a.x) < x) {
                    intersect = !intersect;
                }
            }
        }

        return intersect;
    }

    public static boolean aabbPoint(AABB aabb, float x, float y) {
        return aabb.getX() <= x && x <= aabb.getX() + aabb.getWidth() &&
                aabb.getY() <= y && y <= aabb.getY() + aabb.getHeight();
    }

    private static boolean between(float value, float a, float b) {
        float min = Math.min(a, b);
        float max = Math.max(a, b);

        return min <= value && value <= max;
    }
}
