package com.derbysoft.common.util;

import org.junit.Assert;
import org.junit.Test;

public class IPUtilsTest {

    @Test
    public void testGetHostAddress() {
        Assert.assertNotNull(IPUtils.getHostAddress());
        Assert.assertNotEquals(IPUtils.getHostAddress(), "");
    }

    @Test
    public void testIsIP() {
        Assert.assertFalse(IPUtils.isIP(""));
        Assert.assertFalse(IPUtils.isIP("201.0.0"));
        Assert.assertTrue(IPUtils.isIP("192.168.1.1"));
    }

}
