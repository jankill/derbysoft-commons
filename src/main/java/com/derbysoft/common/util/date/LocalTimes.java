package com.derbysoft.common.util.date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Date;

public class LocalTimes {

    public static LocalTime of(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        int hourOfDay = Integer.parseInt(dateTime.substring(0, 2));
        int minuteOfHour = Integer.parseInt(dateTime.substring(3, 5));
        int secondOfMinute = Integer.parseInt(dateTime.substring(6, 8));
        if (dateTime.length() > 9) {
            int millisOfSecond = Integer.parseInt(dateTime.substring(9));
            return new LocalTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
        return new LocalTime(hourOfDay, minuteOfHour, secondOfMinute);
    }

    public static LocalTime of(long dateTime) {
        return of(new Date(dateTime));
    }

    public static LocalTime of(Date date) {
        return new LocalTime(date);
    }

    public static LocalTime of(Calendar date) {
        return new LocalTime(date);
    }

    public static String format(LocalTime dateTime) {
        return dateTime.toString();
    }

    public static String format(Date date) {
        return format(of(date));
    }

    public static String format(Calendar date) {
        return format(of(date));
    }
}
