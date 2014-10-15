package com.derbysoft.common.util.date;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DateRangeIteratorTest {

    public static final String FROM = "2000-01-01";
    public static final String MID = "2000-01-02";
    public static final String TO = "2000-01-03";

    @Test
    public void testNoOptions() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(FROM, TO);
        List<LocalDate> localDates = toList(dateRangeIterator);
        Assert.assertEquals(3, localDates.size());
        Assert.assertEquals(FROM, localDates.get(0).toString());
        Assert.assertEquals(MID, localDates.get(1).toString());
        Assert.assertEquals(TO, localDates.get(2).toString());
    }

    @Test
    public void testSwapFromAndTo() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(TO, FROM);
        List<LocalDate> localDates = toList(dateRangeIterator);
        Assert.assertEquals(0, localDates.size());
    }

    @Test
    public void testExcludeFrom() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(FROM, TO, DateRangeIterator.Option.EXCLUDE_FROM);
        List<LocalDate> localDates = toList(dateRangeIterator);
        Assert.assertEquals(2, localDates.size());
        Assert.assertEquals(MID, localDates.get(0).toString());
        Assert.assertEquals(TO, localDates.get(1).toString());
    }

    @Test
    public void testExcludeTo() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(FROM, TO, DateRangeIterator.Option.EXCLUDE_TO);
        List<LocalDate> localDates = toList(dateRangeIterator);
        Assert.assertEquals(2, localDates.size());
        Assert.assertEquals(FROM, localDates.get(0).toString());
        Assert.assertEquals(MID, localDates.get(1).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWithoutFrom() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(null, TO, DateRangeIterator.Option.EXCLUDE_TO);
    }

    @Test(expected = NullPointerException.class)
    public void testWithoutTo() {
        DateRangeIterator dateRangeIterator = DateRangeIterator.of(FROM, null, DateRangeIterator.Option.EXCLUDE_TO);
    }

    private List<LocalDate> toList(DateRangeIterator dateRangeIterator) {
        List<LocalDate> localDates = new ArrayList<LocalDate>();
        for (LocalDate localDate : dateRangeIterator) {
            localDates.add(localDate);
        }
        return localDates;
    }
}
