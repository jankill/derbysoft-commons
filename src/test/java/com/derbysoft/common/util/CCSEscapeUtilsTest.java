package com.derbysoft.common.util;

import org.junit.Assert;
import org.junit.Test;

public class CCSEscapeUtilsTest {
    @Test
    public void testEscape() {
        Assert.assertEquals("&amp;&cr;&lf;&cln;&vb;", CCSEscapeUtils.escape("&\r\n:|"));
        Assert.assertNull(CCSEscapeUtils.escape(null));
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("&\r\n:|", CCSEscapeUtils.unescape("&amp;&cr;&lf;&cln;&vb;"));
        Assert.assertNull(CCSEscapeUtils.unescape(null));
    }
}
