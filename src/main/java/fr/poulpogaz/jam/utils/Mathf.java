package fr.poulpogaz.jam.utils;

public class Mathf {

    public static final float TWO_PI = (float) (2 * Math.PI);
    public static final float PI = (float) (Math.PI);
    public static final float PI_2 = (float) (Math.PI / 2);
    public static final float PI_4 = (float) (Math.PI / 4);

    public static float pow(float a, float b) {
        if (b == 2) {
            return a * a;
        } else {
            return (float) Math.pow(a, b);
        }
    }
}
