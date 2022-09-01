package fr.poulpogaz.jam.utils;

import java.util.Objects;

public class Utils {

    private static final String[] ZERO = new String[] {
            "",
            "0",
            "00",
            "000",
            "0000",
            "00000",
            "000000",
            "0000000",
            "00000000",
            "000000000",
    };

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

    public static String toString(int score, int minZero) {
        if (score >= 1e9) {
            return String.valueOf(score);
        } else if (score <= 0) {
            return ZERO[minZero];
        } else {
            int zero = minZero - Mathf.nDigit(score);

            return ZERO[zero] + score;
        }
    }
}
