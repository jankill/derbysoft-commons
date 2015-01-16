package com.derbysoft.common.util;

import org.junit.Assert;
import org.junit.Test;

public class SplitUtilsTest {

    public static final String[] EXPECTED = new String[]{"Hello", "World"};

    @Test
    public void splitBySpace() {
        String[] actual = SplitUtils.split("Hello World");
        Assert.assertArrayEquals(EXPECTED, actual);
    }

    @Test
    public void splitByComma() {
        String[] actual = SplitUtils.split("Hello,World");
        Assert.assertArrayEquals(EXPECTED, actual);
    }

    @Test
    public void splitBySemicolon() {
        String[] actual = SplitUtils.split("Hello;World");
        Assert.assertArrayEquals(EXPECTED, actual);
    }

    @Test
    public void splitByReturn() {
        String[] actual = SplitUtils.split("Hello\rWorld");
        Assert.assertArrayEquals(EXPECTED, actual);
    }

    @Test
    public void splitByNewLine() {
        String[] actual = SplitUtils.split("Hello\nWorld");
        Assert.assertArrayEquals(EXPECTED, actual);
    }

    @Test
    public void splitByMixedSeparator() {
        String[] actual = SplitUtils.split("Hello,;\r\nWorld");
        Assert.assertArrayEquals(EXPECTED, actual);
    }
}
