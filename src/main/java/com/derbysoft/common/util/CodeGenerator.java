package com.derbysoft.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * @author zhupan panos.zhu@gmail.com
 * @version 1.8
 */
public class CodeGenerator {

    private static final int I36 = 36;

    private static final long Q36_3 = I36 * I36 * I36;

    private static final Random RANDOM = new Random();

    public static String generate() {
        String value = Long.toString((long) ((RANDOM.nextDouble() + 1.0) * Q36_3), I36);
        return (getTime() + right(value, 3)).toUpperCase();
    }

    private static ThreadLocal threadLocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat("ddHHmm");
        }
    };

    private static String getTime() {
        Calendar calendar = Calendar.getInstance();
        String time = ((SimpleDateFormat) threadLocal.get()).format(calendar.getTime());
        String month = Integer.toHexString(calendar.get(Calendar.MONTH) + 1);
        int year = calendar.get(Calendar.YEAR) % 100 % I36;
        int milliSecond = calendar.get(Calendar.MILLISECOND);
        String milliSecondStr = to36(milliSecond);
        if (milliSecondStr.length() < 2) {
            milliSecondStr = "0" + milliSecondStr;
        }
        return to36(year) + month + time + milliSecondStr;
    }

    private static String to36(int value) {
        return Integer.toString(value, I36);
    }

    private static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

}
