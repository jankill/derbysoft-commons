package com.derbysoft.common.util;

import com.derbysoft.common.util.date.Dates;
import com.derbysoft.common.util.date.LocalDateTimes;
import com.derbysoft.common.util.date.LocalDates;
import com.derbysoft.common.util.date.LocalTimes;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class PerformanceLogBuilderTest {
    private PerformanceLogBuilder builder = new PerformanceLogBuilder();

    @Test
    public void testAppendWithValue() {
        builder.append("Date", Dates.of("2000-01-01"));
        builder.append("LocalDate", LocalDates.of("2000-01-01"));
        builder.append("LocalTime", LocalTimes.of("12:00:00.000"));
        builder.append("LocalDateTime", LocalDateTimes.of("2000-01-01 12:00:00.000"));
        builder.append("Object", "<=>\r\n");
        Assert.assertEquals("<Date=2000-01-01> <LocalDate=2000-01-01> <LocalTime=12:00:00.000> <LocalDateTime=2000-01-01T12:00:00.000> <Object=&lt;&eq;&gt;&#13;&#10;>", builder.toString());
    }

    @Test
    public void testAppendWithNullValue() {
        builder.append("Date", (Date) null);
        builder.append("LocalDate", (LocalDate) null);
        builder.append("LocalTime", (LocalTime) null);
        builder.append("LocalDateTime", (LocalDateTime) null);
        builder.append("Object", (Object) null);
        Assert.assertEquals("", builder.toString());
    }
}
