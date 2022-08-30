package fr.poulpogaz.jam.engine;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;

public class HitBoxUtils {

    public static AABB createAABB(Polygon last, float x, float y, float w, float h) {
        if (last instanceof AABB a) {
            a.set(x, y, w, h);
            return a;
        } else {
            return new AABB(x, y, w, h);
        }
    }
}
