package com.derbysoft.common.util.date;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    @Test
    public void test() {
        assertTrue(DateValidator.validate("2018-01-01"));
        assertTrue(DateValidator.validate("0001-01-01"));
        assertFalse(DateValidator.validate("0000-01-01"));
        assertFalse(DateValidator.validate("20120101"));
        assertFalse(DateValidator.validate("2012/01/01"));
        assertFalse(DateValidator.validate(""));
        assertFalse(DateValidator.validate(null));
        assertFalse(DateValidator.validate("dsdfsafs"));
    }

}
