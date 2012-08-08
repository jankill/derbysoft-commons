package com.derbysoft.common.util.date;

import java.util.Calendar;
import java.util.Date;

public abstract class Calendars {

    public static Calendar of(String source) {
        return of(Dates.of(source));
    }

    public static Calendar of(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

}
