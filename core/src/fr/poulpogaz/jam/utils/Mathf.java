package fr.poulpogaz.jam.utils;

import java.util.Random;

public class Mathf {

    public static final float TWO_PI = (float) (2 * Math.PI);
    public static final float PI = (float) (Math.PI);
    public static final float PI_2 = (float) (Math.PI / 2);
    public static final float PI_4 = (float) (Math.PI / 4);

    public static final float LN_2 = (float) Math.log(2f);

    public static final Random RANDOM = new Random();

    public static float log(float a, float b) {
        return (float) (Math.log(a) / b);
    }

    public static float pow(float a, float b) {
        if (b == 2) {
            return a * a;
        } else {
            return (float) Math.pow(a, b);
        }
    }

    public static int absMod(int a, int mod) {
        int b = a % mod;

        if (b < 0) {
            return b + mod;
        } else {
            return b;
        }
    }

    public static float absMod(float a, float mod) {
        float b = a % mod;

        if (b < 0) {
            return b + mod;
        } else {
            return b;
        }
    }

    public static float random(float a, float b) {
        if (a == b) {
            return a;
        } else if (a < b) {
            return RANDOM.nextFloat() * (b - a) + a;
        } else {
            return RANDOM.nextFloat() * (a - b) + b;
        }
    }
}
