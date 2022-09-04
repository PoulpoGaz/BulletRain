package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.Vector2;

public class Edge implements HitBox {

    private final Vector2 a = new Vector2();
    private final Vector2 b = new Vector2();

    @Override
    public boolean collide(HitBox b) {
        return CollideHelper.edge(this, b);
    }

    @Override
    public void getAABB(AABB dest) {
        if (a.x > b.x) {
            dest.setX(b.x);
            dest.setWidth(a.x - b.x);
        } else {
            dest.setX(a.x);
            dest.setWidth(b.x - a.x);
        }

        if (a.y > b.y) {
            dest.setY(b.y);
            dest.setHeight(a.y - b.y);
        } else {
            dest.setY(a.y);
            dest.setHeight(b.y - a.y);
        }
    }

    public Vector2 getA() {
        return a;
    }

    public Vector2 getB() {
        return b;
    }
}
