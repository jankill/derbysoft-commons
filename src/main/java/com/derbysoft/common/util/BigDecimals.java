package com.derbysoft.common.util;

import java.math.BigDecimal;

/**
 * @author zhupan
 * @version 1.0
 * @since 2012-5-25
 */
public abstract class BigDecimals {

    private static final int SCALE = 3;

    private static final double FOR_SCALE = 10000d;

    public static BigDecimal roundUp(BigDecimal value, int i) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf((Math.floor(value.doubleValue() * FOR_SCALE)) / FOR_SCALE).setScale(i, BigDecimal.ROUND_UP);
    }

    public static BigDecimal roundUp(BigDecimal value) {
        return roundUp(value, SCALE);
    }

    public static BigDecimal roundUp(Double value) {
        if (value == null) {
            return null;
        }
        return roundUp(BigDecimal.valueOf(value));
    }

    public static BigDecimal roundUp(double value, int i) {
        return roundUp(BigDecimal.valueOf(value), i);
    }

    public static BigDecimal roundUp(double value) {
        return roundUp(BigDecimal.valueOf(value));
    }

    public static BigDecimal roundUp(Float value) {
        if (value == null) {
            return null;
        }
        return roundUp(BigDecimal.valueOf(value));
    }

    public static BigDecimal roundUp(float value) {
        return roundUp(BigDecimal.valueOf(value));
    }

    public static BigDecimal roundUp(String value) {
        if (value == null) {
            return null;
        }
        return roundUp(new BigDecimal(value));
    }

    public static BigDecimal roundUp(String value, int i) {
        if (value == null) {
            return null;
        }
        return roundUp(new BigDecimal(value), i);
    }

    public static BigDecimal format(BigDecimal bigDecimal) {
        return (bigDecimal != null) ? new BigDecimal(removeEnd0(bigDecimal.toString())) : null;
    }

    private static String removeEnd0(String value) {
        if (!value.endsWith("0")) {
            return value;
        }
        return removeEnd0(value.substring(0, value.length() - 1));
    }


}
