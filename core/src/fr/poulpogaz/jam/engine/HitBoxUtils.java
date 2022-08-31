package fr.poulpogaz.jam.engine;

public class HitBoxUtils {

    public static AABB createAABB(HitBox last, float x, float y, float w, float h) {
        if (last instanceof AABB) {
            AABB a = (AABB) last;
            a.set(x, y, w, h);
            return a;
        } else {
            return new AABB(x, y, w, h);
        }
    }
}
