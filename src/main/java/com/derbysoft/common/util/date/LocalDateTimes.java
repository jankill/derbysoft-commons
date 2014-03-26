package com.derbysoft.common.util.date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

public class LocalDateTimes {

    public static LocalDateTime of(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int monthOfYear = Integer.parseInt(dateTime.substring(5, 7));
        int dayOfMonth = Integer.parseInt(dateTime.substring(8, 10));
        int hourOfDay = Integer.parseInt(dateTime.substring(11, 13));
        int minuteOfHour = Integer.parseInt(dateTime.substring(14, 16));
        int secondOfMinute = Integer.parseInt(dateTime.substring(17, 19));
        if (dateTime.length() > 20) {
            int millisOfSecond = Integer.parseInt(dateTime.substring(20));
            return new LocalDateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
        return new LocalDateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
    }

    public static LocalDateTime of(long dateTime) {
        return of(new Date(dateTime));
    }

    public static LocalDateTime of(Date date) {
        return new LocalDateTime(date);
    }

    public static LocalDateTime of(Calendar date) {
        return new LocalDateTime(date);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.toString();
    }

    public static String format(Date date) {
        return format(of(date));
    }

    public static String format(Calendar date) {
        return format(of(date));
    }
}
