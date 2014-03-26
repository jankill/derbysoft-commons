package com.derbysoft.common.util;

public abstract class Doubles {

    public static final double DELTA = 0.000001;

    public static boolean equals(Double d1, Double d2) {
        return equals(d1, d2, DELTA);
    }

    public static boolean equals(Double d1, Double d2, double delta) {
        if (d1 == null || d2 == null) {
            return d1 == null && d2 == null;
        }
        return Math.abs(d1 - d2) <= delta;
    }
}
