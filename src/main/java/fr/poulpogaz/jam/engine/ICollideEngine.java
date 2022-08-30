package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.*;
import org.joml.Vector2f;

import java.util.List;

public interface ICollideEngine {

    default boolean pointPoint(Vector2f p1, Vector2f p2) {
        return pointPoint(new Point(p1), new Point(p2), null);
    }

    default boolean pointEdge(Vector2f p, Edge e) {
        return pointEdge(new Point(p), e, null);
    }

    default boolean pointAABB(Vector2f p, AABB a) {
        return pointAABB(new Point(p), a, null);
    }

    default boolean pointCircle(Vector2f p, Circle c) {
        return pointCircle(new Point(p), c, null);
    }

    default boolean pointConvex(Vector2f p, ConvexPolygon c) {
        return pointConvex(new Point(p), c, null);
    }

    default boolean pointConcave(Vector2f p, ConcavePolygon c) {
        return pointConcave(new Point(p), c, null);
    }

    default boolean pointPolygon(Vector2f p, Polygon polygon) {
        return pointPolygon(new Point(p), polygon, null);
    }




    default boolean pointPoint(Point p1, Point p2) {
        return pointPoint(p1, p2, null);
    }

    default boolean pointEdge(Point p, Edge e) {
        return pointEdge(p, e, null);
    }

    default boolean pointAABB(Point p, AABB a) {
        return pointAABB(p, a, null);
    }

    default boolean pointCircle(Point p, Circle c) {
        return pointCircle(p, c, null);
    }

    default boolean pointConvex(Point p, ConvexPolygon c) {
        return pointConvex(p, c, null);
    }

    default boolean pointConcave(Point p, ConcavePolygon c) {
        return pointConcave(p, c, null);
    }

    default boolean pointPolygon(Point p, Polygon polygon) {
        return pointPolygon(p, polygon, null);
    }



    default boolean edgeEdge(Edge e1, Edge e2) {
        return edgeEdge(e1, e2, null);
    }

    default boolean edgeAABB(Edge e, AABB a) {
        return edgeAABB(e, a, null);
    }

    default boolean edgeCircle(Edge e, Circle c) {
        return edgeCircle(e, c, null);
    }

    default boolean edgeConvex(Edge e, ConvexPolygon c) {
        return edgeConvex(e, c, null);
    }

    default boolean edgeConcave(Edge e, ConcavePolygon c) {
        return edgeConcave(e, c, null);
    }

    default boolean edgePolygon(Edge e, Polygon p) {
        return edgePolygon(e, p, null);
    }


    default boolean aabbAABB(AABB a, AABB b) {
        return aabbAABB(a, b, null);
    }

    default boolean aabbCircle(AABB a, Circle c) {
        return aabbCircle(a, c, null);
    }

    default boolean aabbConvex(AABB a, ConvexPolygon p) {
        return aabbConvex(a, p, null);
    }

    default boolean aabbConcave(AABB a, ConcavePolygon p) {
        return aabbConcave(a, p, null);
    }

    default boolean aabbPolygon(AABB a, Polygon p) {
        return aabbPolygon(a, p, null);
    }


    default boolean circleCircle(Circle c1, Circle c2) {
        return circleCircle(c1, c2, null);
    }

    default boolean circleConvex(Circle c, ConvexPolygon convex) {
        return circleConvex(c, convex, null);
    }

    default boolean circleConcave(Circle c, ConcavePolygon concave) {
        return circleConcave(c, concave, null);
    }

    default boolean circlePolygon(Circle c, Polygon p) {
        return circlePolygon(c, p, null);
    }


    default boolean convexConvex(ConvexPolygon c1, ConvexPolygon c2) {
        return convexConvex(c1, c2, null);
    }

    default boolean convexConcave(ConvexPolygon c1, ConcavePolygon c2) {
        return convexConcave(c1, c2, null);
    }

    default boolean convexPolygon(ConvexPolygon c, Polygon p) {
        return convexPolygon(c, p, null);
    }


    default boolean concaveConcave(ConcavePolygon c1, ConcavePolygon c2) {
        return concaveConcave(c1, c2, null);
    }

    default boolean concavePolygon(ConcavePolygon c, Polygon p) {
        return concavePolygon(c, p, null);
    }


    default boolean polygonPolygon(Polygon p1, Polygon p2) {
        return polygonPolygon(p1, p2, null);
    }






    default boolean pointPoint(Vector2f p1, Vector2f p2, List<Vector2f> intersectionPoints) {
        return pointPoint(new Point(p1), new Point(p2), intersectionPoints);
    }

    default boolean pointEdge(Vector2f p, Edge e, List<Vector2f> intersectionPoints) {
        return pointEdge(new Point(p), e, intersectionPoints);
    }

    default boolean pointAABB(Vector2f p, AABB a, List<Vector2f> intersectionPoints) {
        return pointAABB(new Point(p), a, intersectionPoints);
    }

    default boolean pointCircle(Vector2f p, Circle c, List<Vector2f> intersectionPoints) {
        return pointCircle(new Point(p), c, intersectionPoints);
    }

    default boolean pointConvex(Vector2f p, ConvexPolygon c, List<Vector2f> intersectionPoints) {
        return pointConvex(new Point(p), c, intersectionPoints);
    }

    default boolean pointConcave(Vector2f p, ConcavePolygon c, List<Vector2f> intersectionPoints) {
        return pointConcave(new Point(p), c, intersectionPoints);
    }

    default boolean pointPolygon(Vector2f p, Polygon polygon, List<Vector2f> intersectionPoints) {
        return pointPolygon(new Point(p), polygon, intersectionPoints);
    }




    boolean pointPoint(Point p1, Point p2, List<Vector2f> intersectionPoints);

    boolean pointEdge(Point p, Edge e, List<Vector2f> intersectionPoints);

    boolean pointAABB(Point p, AABB a, List<Vector2f> intersectionPoints);

    boolean pointCircle(Point p, Circle c, List<Vector2f> intersectionPoints);

    boolean pointConvex(Point p, ConvexPolygon c, List<Vector2f> intersectionPoints);

    boolean pointConcave(Point p, ConcavePolygon c, List<Vector2f> intersectionPoints);

    boolean pointPolygon(Point p, Polygon polygon, List<Vector2f> intersectionPoints);



    boolean edgeEdge(Edge e1, Edge e2, List<Vector2f> intersectionPoints);

    boolean edgeAABB(Edge e, AABB a, List<Vector2f> intersectionPoints);

    boolean edgeCircle(Edge e, Circle c, List<Vector2f> intersectionPoints);

    boolean edgeConvex(Edge e, ConvexPolygon c, List<Vector2f> intersectionPoints);

    boolean edgeConcave(Edge e, ConcavePolygon c, List<Vector2f> intersectionPoints);

    boolean edgePolygon(Edge e, Polygon p, List<Vector2f> intersectionPoints);


    boolean aabbAABB(AABB a, AABB b, List<Vector2f> intersectionPoints);

    boolean aabbCircle(AABB a, Circle c, List<Vector2f> intersectionPoints);

    boolean aabbConvex(AABB a, ConvexPolygon p, List<Vector2f> intersectionPoints);

    boolean aabbConcave(AABB a, ConcavePolygon p, List<Vector2f> intersectionPoints);

    boolean aabbPolygon(AABB a, Polygon p, List<Vector2f> intersectionPoints);


    boolean circleCircle(Circle c1, Circle c2, List<Vector2f> intersectionPoints);

    boolean circleConvex(Circle c, ConvexPolygon convex, List<Vector2f> intersectionPoints);

    boolean circleConcave(Circle c, ConcavePolygon concave, List<Vector2f> intersectionPoints);

    boolean circlePolygon(Circle c, Polygon p, List<Vector2f> intersectionPoints);


    boolean convexConvex(ConvexPolygon c1, ConvexPolygon c2, List<Vector2f> intersectionPoints);

    boolean convexConcave(ConvexPolygon c1, ConcavePolygon c2, List<Vector2f> intersectionPoints);

    boolean convexPolygon(ConvexPolygon c, Polygon p, List<Vector2f> intersectionPoints);


    boolean concaveConcave(ConcavePolygon c1, ConcavePolygon c2, List<Vector2f> intersectionPoints);

    boolean concavePolygon(ConcavePolygon c, Polygon p, List<Vector2f> intersectionPoints);


    boolean polygonPolygon(Polygon p1, Polygon p2, List<Vector2f> intersectionPoints);



    void calculateIntersection(boolean intersection);

    boolean isCalculateIntersection();
}