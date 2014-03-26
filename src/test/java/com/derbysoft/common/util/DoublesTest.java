package com.derbysoft.common.util;

import org.junit.Assert;
import org.junit.Test;

public class DoublesTest {
    @Test
    public void testEquals() {
        Assert.assertTrue(Doubles.equals(null, null));
        Assert.assertTrue(Doubles.equals(0.1, 0.1));
        Assert.assertTrue(Doubles.equals(0.000001, 0.000001));
        Assert.assertTrue(Doubles.equals(0.000001, 0.000002));
        Assert.assertFalse(Doubles.equals(0.00001, 0.00002));
        Assert.assertTrue(Doubles.equals(0.00001, 0.00002, 0.00001));
        Assert.assertFalse(Doubles.equals(0.1, null));
        Assert.assertFalse(Doubles.equals(null, 0.1));
    }
}
