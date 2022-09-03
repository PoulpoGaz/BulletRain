package fr.poulpogaz.jam.engine;

public class AABB implements HitBox {

    private float x;
    private float y;
    private float width;
    private float height;

    public AABB() {
    }

    public AABB(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean collide(HitBox b) {
        return CollideHelper.aabb(this, b);
    }

    @Override
    public void getAABB(AABB dest) {
        dest.set(x, y, width, height);
    }

    public void set(float x, float y, float w, float h) {
        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
