package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class ExceptionUtilsTest {
    @Test
    public void testToString1() {
        try {
            System.setProperty(ExceptionUtils.MIN_LINE_COUNT_KEY, "8");
            System.setProperty(ExceptionUtils.KEY_WORDS_KEY, null);
            throw new IllegalArgumentException("HelloWorld");
        } catch (Exception e) {
            String exceptionString = ExceptionUtils.toString(e);
            String[] split = StringUtils.split(exceptionString, "\n");
            Assert.assertEquals(8, split.length);
        }
    }

    @Test
    public void testToString2() {
        try {
            System.setProperty(ExceptionUtils.MAX_LINE_COUNT_KEY, "20");
            System.setProperty(ExceptionUtils.KEY_WORDS_KEY, "At layer");
            makeException(30);
        } catch (Exception e) {
            String exceptionString = ExceptionUtils.toString(e);
            String[] split = StringUtils.split(exceptionString, "\n");
            Assert.assertEquals(20, split.length);
        }
    }

    @Test
    public void testToString3() {
        try {
            System.setProperty(ExceptionUtils.MAX_LINE_COUNT_KEY, "10");
            System.setProperty(ExceptionUtils.KEY_WORDS_KEY, "At layer");
            makeException(15);
        } catch (Exception e) {
            String exceptionString = ExceptionUtils.toString(e);
            String[] split = StringUtils.split(exceptionString, "\n");
            Assert.assertEquals(10, split.length);
        }
    }

    private void makeException(int layer) {
        if (layer == 0) {
            throw new IllegalArgumentException("HelloWorld");
        }
        try {
            makeException(layer - 1);
        } catch (Exception e) {
            throw new IllegalArgumentException("At layer " + layer, e);
        }
    }
}
