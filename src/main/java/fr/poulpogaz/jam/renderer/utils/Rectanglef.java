package fr.poulpogaz.jam.renderer.utils;

public class Rectanglef {

    private float x;
    private float y;
    private float width;
    private float height;

    public Rectanglef() {
    }

    public Rectanglef(float x, float y, float width, float height) {
        set(x, y, width, height);
    }

    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
}