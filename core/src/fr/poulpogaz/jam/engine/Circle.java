package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.math.Vector2;

public class Circle implements HitBox {

    private Vector2 center;
    private float radius;
    private float radiusSquared = -1;

    public Circle(Vector2 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean collide(HitBox b) {
        return CollideHelper.circle(this, b);
    }

    @Override
    public void getAABB(AABB dest) {
        float radSum = radius + radius;

        dest.set(center.x - radius, center.y - radius, radSum, radSum);
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (this.radius != radius) {
            this.radius = radius;
            radiusSquared = -1;
        }
    }

    public float getRadiusSquared() {
        if (radiusSquared < 0) {
            radiusSquared = radius * radius;
        }

        return radiusSquared;
    }
}
