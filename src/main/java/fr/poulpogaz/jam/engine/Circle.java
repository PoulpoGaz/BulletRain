package fr.poulpogaz.jam.engine;

import org.joml.Vector2f;

public class Circle implements HitBox {

    private Vector2f center;
    private float radius;
    private float radiusSquared = -1;

    public Circle(Vector2f center, float radius) {
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

    public Vector2f getCenter() {
        return center;
    }

    public void setCenter(Vector2f center) {
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
