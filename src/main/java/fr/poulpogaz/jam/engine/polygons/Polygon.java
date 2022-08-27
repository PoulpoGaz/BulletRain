package fr.poulpogaz.jam.engine.polygons;

import fr.poulpogaz.jam.engine.ICollideEngine;
import fr.poulpogaz.jam.engine.Report;
import org.joml.Vector2f;

public interface Polygon {

    Report collide(Polygon polygon);

    boolean isCircle();

    boolean isConcave();

    boolean isConvex();

    boolean isAABB();

    boolean isEdge();

    boolean isPoint();

    AABB getAABB();

    Vector2f[] models();

    Vector2f[] points();

    Vector2f center();

    void setCenter(Vector2f center);

    ICollideEngine getCollideEngine();

    void setCollideEngine(ICollideEngine engine);
}