package com.derbysoft.common.util;

import com.derbysoft.common.util.date.LocalDates;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author zhupan
 * @version 1.0
 * @since 9/11/13
 */
public class LocalDatesTest {

    @Test
    public void test() {
        assertEquals(LocalDates.daysBetween("2013-01-01", "2013-01-01"), 0);
        assertEquals(LocalDates.daysBetween("2013-01-01", "2013-01-02"), 1);
        assertEquals(LocalDates.plus("2013-01-01", 1), "2013-01-02");
        assertEquals(LocalDates.minusDays("2013-01-02", 1), LocalDates.of("2013-01-01"));
        assertEquals(createExpected(), LocalDates.getStayDateRangeDays("2013-01-01", "2013-01-05"));
    }

    private List<LocalDate> createExpected() {
        List<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(new LocalDate("2013-01-01"));
        dates.add(new LocalDate("2013-01-02"));
        dates.add(new LocalDate("2013-01-03"));
        dates.add(new LocalDate("2013-01-04"));
        return dates;
    }

}
