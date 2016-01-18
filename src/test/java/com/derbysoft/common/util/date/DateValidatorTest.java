package com.derbysoft.common.util.date;

import org.junit.Test;

import java.text.SimpleDateFormat;

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
    public void testPerformance() {
        int count = 1000000;
        String date = "2015-02-29";
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            validateDate(date);
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            DateValidator.validate(date);
        }
        System.out.println(System.currentTimeMillis() - start);

    }


    public static boolean validateDate(String date) {
        try {
            Dates.of(date, threadLocal.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);
            return simpleDateFormat;
        }
    };
}
