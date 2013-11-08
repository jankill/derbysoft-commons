package com.derbysoft.common.util.date;

import com.derbysoft.common.exception.SystemException;

import java.text.ParseException;
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
        try {
            return getDayFormatter().parse(date);
        } catch (ParseException e) {
            throw new SystemException("parse date [" + date + "] failed in use [" + getDayFormatter() + "]", e);
        }
    }

    public static String format(Date date) {
        return getDayFormatter().format(date);
    }

    public static SimpleDateFormat getDayFormatter() {
        return threadLocal.get();
    }

    public static Date addDays(Date date, int value) {
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
