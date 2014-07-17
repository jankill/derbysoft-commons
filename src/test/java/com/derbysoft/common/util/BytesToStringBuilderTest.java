package com.derbysoft.common.util;

import com.derbysoft.common.JUnitUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BytesToStringBuilderTest {

    @Test
    public void testToString() {
        byte[] bytes = new byte[4];
        bytes[0] = -128;
        bytes[1] = 99;
        bytes[2] = 127;
        bytes[3] = 35;
        assertEquals("-3k,2r,3j,z", BytesToStringBuilder.toString(bytes));
    }

    @Test
    public void testToBytes() {
        String value = "-3k,2r,3j,z";
        byte[] bytes = new byte[4];
        bytes[0] = -128;
        bytes[1] = 99;
        bytes[2] = 127;
        bytes[3] = 35;
        JUnitUtils.assertXMLEquals(bytes, BytesToStringBuilder.toBytes(value));
    }

}
