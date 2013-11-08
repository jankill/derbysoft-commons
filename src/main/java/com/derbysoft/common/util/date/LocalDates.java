package com.derbysoft.common.util.date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhupan
 * @version 1.0
 * @since 9/11/13
 */
public abstract class LocalDates {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final int ONE = 1;

    public static String plus(String date, int day) {
        return of(date).plusDays(day).toString(YYYY_MM_DD);
    }

    public static String plus(Date date, int day) {
        return of(date).plusDays(day).toString(YYYY_MM_DD);
    }

    public static LocalDate plusDays(String date, int day) {
        return of(date).plusDays(day);
    }

    public static LocalDate plusDays(Date date, int day) {
        return of(date).plusDays(day);
    }

    public static LocalDate minusDays(Date date, int day) {
        return of(date).minusDays(day);
    }

    public static LocalDate minusDays(String date, int day) {
        return of(date).minusDays(day);
    }

    public static LocalDate prev(Date date) {
        return minusDays(date, ONE);
    }

    public static String prev(String date) {
        return format(minusDays(date, ONE));
    }

    public static LocalDate prev(LocalDate date) {
        return date.minusDays(ONE);
    }

    public static LocalDate next(Date date) {
        return plusDays(date, ONE);
    }

    public static LocalDate next(LocalDate date) {
        return date.plusDays(ONE);
    }

    public static String next(String date) {
        return format(plusDays(date, ONE));
    }

    public static LocalDate of(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int monthOfYear = Integer.parseInt(date.substring(5, 7));
        int dayOfMonth = Integer.parseInt(date.substring(8));
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    public static String format(LocalDate date) {
        return date.toString(YYYY_MM_DD);
    }

    public static String format(Date date) {
        return format(of(date));
    }

    public static String format(Calendar date) {
        return format(of(date));
    }

    public static LocalDate of(long dateTime) {
        return of(new Date(dateTime));
    }

    public static LocalDate of(Date date) {
        return new LocalDate(date);
    }

    public static LocalDate of(Calendar date) {
        return new LocalDate(date);
    }

    public static int daysBetween(LocalDate startDate, LocalDate endDate) {
        return Days.daysBetween(startDate, endDate).getDays();
    }

    public static int daysBetween(String startDate, String endDate) {
        return daysBetween(of(startDate), of(endDate));
    }

    public static int daysBetween(Date startDate, String endDate) {
        return daysBetween(of(startDate), of(endDate));
    }

    public static int daysBetween(String startDate, Date endDate) {
        return daysBetween(of(startDate), of(endDate));
    }

    public static List<LocalDate> getStayDateRangeDays(LocalDate checkInDate, LocalDate checkOutDate) {
        List<LocalDate> result = new ArrayList<LocalDate>();
        for (LocalDate day = checkInDate; day.isBefore(checkOutDate); day = day.plusDays(1)) {
            result.add(day);
        }
        return result;
    }

    public static List<LocalDate> getStayDateRangeDays(String checkInDate, String checkOutDate) {
        return getStayDateRangeDays(of(checkInDate), of(checkOutDate));
    }

    public static List<LocalDate> getDateRangeDays(String startDate, String endDate) {
        return getStayDateRangeDays(startDate, next(endDate));
    }

    public static List<LocalDate> getDateRangeDays(Calendar startDate, Calendar endDate) {
        return getStayDateRangeDays(of(startDate), next(of(endDate)));
    }

    public static List<LocalDate> getDateRangeDays(LocalDate startDate, LocalDate endDate) {
        return getStayDateRangeDays(startDate, next(endDate));
    }

}