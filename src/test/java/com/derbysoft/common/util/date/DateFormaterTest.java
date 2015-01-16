package com.derbysoft.common.util.date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateFormaterTest {

    @Test
    public void test() {
        assertEquals("2015-02-01", DateFormater.format("2015/2/1"));
        assertEquals("2015-02-01", DateFormater.format("2015-2-1"));
        assertEquals("2015-12-01", DateFormater.format("2015/12/1"));
        assertEquals("2015-12-01", DateFormater.format("12/1/2015"));
        assertEquals("2015-12-01", DateFormater.format("12/01/2015"));
    }

}
