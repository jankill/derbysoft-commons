package com.derbysoft.common.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateFormater {

    private static ThreadLocal<SimpleDateFormat> yyyymd = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/M/d");
        }
    };

    private static ThreadLocal<SimpleDateFormat> mdyyyy = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("M/d/yyyy");
        }
    };

    private static Date of(String date) {
        try {
            return Dates.of(date);
        } catch (IllegalArgumentException e) {
            if (StringUtils.split(date, "/")[0].length() < 4) {
                return Dates.of(date, mdyyyy.get());
            }
            return Dates.of(date, yyyymd.get());
        }
    }

    public static String format(String date) {
        return Dates.format(of(date));
    }

}
