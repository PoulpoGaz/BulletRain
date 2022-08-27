package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.*;
import org.joml.Vector2f;

public interface ICollideEngine {

    default Report pointPoint(Vector2f p1, Vector2f p2) {
        return pointPoint(new Point(p1), new Point(p2));
    }

    default Report pointEdge(Vector2f p, Edge e) {
        return pointEdge(new Point(p), e);
    }

    default Report pointAABB(Vector2f p, AABB a) {
        return pointAABB(new Point(p), a);
    }

    default Report pointCircle(Vector2f p, Circle c) {
        return pointCircle(new Point(p), c);
    }

    default Report pointConvex(Vector2f p, ConvexPolygon c) {
        return pointConvex(new Point(p), c);
    }

    default Report pointConcave(Vector2f p, ConcavePolygon c) {
        return pointConcave(new Point(p), c);
    }

    default Report pointPolygon(Vector2f p, Polygon polygon) {
        return pointPolygon(new Point(p), polygon);
    }




    Report pointPoint(Point p1, Point p2);

    Report pointEdge(Point p, Edge e);

    Report pointAABB(Point p, AABB a);

    Report pointCircle(Point p, Circle c);

    Report pointConvex(Point p, ConvexPolygon c);

    Report pointConcave(Point p, ConcavePolygon c);

    Report pointPolygon(Point p, Polygon polygon);



    Report edgeEdge(Edge e1, Edge e2);

    Report edgeAABB(Edge e, AABB a);

    Report edgeCircle(Edge e, Circle c);

    Report edgeConvex(Edge e, ConvexPolygon c);

    Report edgeConcave(Edge e, ConcavePolygon c);

    Report edgePolygon(Edge e, Polygon p);


    Report aabbAABB(AABB a, AABB b);

    Report aabbCircle(AABB a, Circle c);

    Report aabbConvex(AABB a, ConvexPolygon p);

    Report aabbConcave(AABB a, ConcavePolygon p);

    Report aabbPolygon(AABB a, Polygon p);


    Report circleCircle(Circle c1, Circle c2);

    Report circleConvex(Circle c, ConvexPolygon convex);

    Report circleConcave(Circle c, ConcavePolygon concave);

    Report circlePolygon(Circle c, Polygon p);


    Report convexConvex(ConvexPolygon c1, ConvexPolygon c2);

    Report convexConcave(ConvexPolygon c1, ConcavePolygon c2);

    Report convexPolygon(ConvexPolygon c, Polygon p);


    Report concaveConcave(ConcavePolygon c1, ConcavePolygon c2);

    Report concavePolygon(ConcavePolygon c, Polygon p);


    Report polygonPolygon(Polygon p1, Polygon p2);


    void calculateIntersection(boolean intersection);

    boolean isCalculateIntersection();
}