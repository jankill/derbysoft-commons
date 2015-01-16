package com.derbysoft.common.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Dates {

    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static Date of(String date) {
        return of(date, getDayFormatter());
    }

    public static Date of(String date, DateFormat dateFormat) {
        try {
            if (StringUtils.isBlank(date)) {
                return null;
            }
            return dateFormat.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date:" + date);
        }
    }

    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return getDayFormatter().format(date);
    }

    public static SimpleDateFormat getDayFormatter() {
        return threadLocal.get();
    }

    public static Date addDays(Date date, int value) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, value);
        return cal.getTime();
    }

    public static String addDays(String date, int day) {
        return format(addDays(of(date), day));
    }

    public static Date prev(Date date) {
        return addDays(date, -1);
    }

    public static String prev(String date) {
        return format(prev(of(date)));
    }

    public static Date next(Date date) {
        return addDays(date, 1);
    }

    public static String next(String date) {
        return format(next(of(date)));
    }

    public static Calendar getUTCCalendar() {
        Calendar calendar = Calendar.getInstance();
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return calendar;
    }
}
