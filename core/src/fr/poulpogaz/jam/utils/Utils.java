package fr.poulpogaz.jam.utils;

import java.util.Objects;

public class Utils {

    public static <T> T requireNonNullElse(T value, T ifNull) {
        if (value == null) {
            return Objects.requireNonNull(ifNull);
        } else {
            return value;
        }
    }

    public static int round2(float v) {
        return Math.round(v * 100) / 100;
    }
}
