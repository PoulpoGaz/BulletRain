package fr.poulpogaz.jam.engine;

public interface HitBox {

    boolean collide(HitBox b);

    void getAABB(AABB dest);
}
