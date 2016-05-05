package com.derbysoft.common.util.date;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    @Test
    public void test() {
        assertFalse(DateValidator.validate("2013-02-30"));
        assertFalse(DateValidator.validate("2013-13-30"));
        assertFalse(DateValidator.validate("2013-14-30"));
        assertFalse(DateValidator.validate("2013-01-32"));
        assertFalse(DateValidator.validate("2013-04-31"));
        assertFalse(DateValidator.validate("2013-06-31"));
        assertTrue(DateValidator.validate("2013-07-31"));
        assertFalse(DateValidator.validate("2013-07-32"));
        assertTrue(DateValidator.validate("2018-01-01"));
        assertTrue(DateValidator.validate("2017-02-10"));
        assertTrue(DateValidator.validate("2017-02-11"));
        assertTrue(DateValidator.validate("2017-02-12"));
        assertTrue(DateValidator.validate("2012-02-29"));
        assertFalse(DateValidator.validate("2013-02-29"));
        assertTrue(DateValidator.validate("2013-02-28"));
        assertFalse(DateValidator.validate("2014-02-29"));
        assertTrue(DateValidator.validate("2014-02-28"));
        assertFalse(DateValidator.validate("2015-02-29"));
        assertTrue(DateValidator.validate("2100-02-28"));
        assertFalse(DateValidator.validate("2100-02-29"));
        assertTrue(DateValidator.validate("2015-02-28"));
        assertTrue(DateValidator.validate("2016-02-15"));
        assertTrue(DateValidator.validate("2017-02-13"));
        assertTrue(DateValidator.validate("2015-02-13"));
        assertTrue(DateValidator.validate("2018-02-13"));
        assertTrue(DateValidator.validate("2016-02-29"));
        assertFalse(DateValidator.validate("2021-02-29"));
        assertTrue(DateValidator.validate("2020-02-29"));
        assertTrue(DateValidator.validate("0001-01-01"));
        assertFalse(DateValidator.validate("0000-01-01"));
        assertFalse(DateValidator.validate("20120101"));
        assertFalse(DateValidator.validate("2012/01/01"));
        assertFalse(DateValidator.validate(""));
        assertFalse(DateValidator.validate(null));
        assertFalse(DateValidator.validate("dsdfsafs"));
    }

    @Test
    public void fixedRangeTest() {
        for (LocalDate localDate : DateRangeIterator.of("2000-01-01", "2100-01-01")) {
            assertTrue(DateValidator.validate(localDate.toString()));
        };
    }
}
