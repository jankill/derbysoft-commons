package com.derbysoft.common.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author zhupan
 * @version 1.0
 * @since 2012-5-25
 */
public class BigDecimalsTest {
    @Test
    public void test() {
        assertEquals(new BigDecimal("100.22"), BigDecimals.roundUp(100.211));
        assertEquals(new BigDecimal("100.20"), BigDecimals.roundUp(100.2));
        assertEquals(new BigDecimal("100.20"), BigDecimals.roundUp(100.2000));
        assertEquals(new BigDecimal("100.21"), BigDecimals.roundUp(100.2001));
        assertEquals(new BigDecimal("100.00"), BigDecimals.roundUp(100));
        assertEquals(new BigDecimal("100.20"), BigDecimals.roundUp("100.2000"));
        assertEquals(new BigDecimal("100.21"), BigDecimals.roundUp("100.2001"));
        String a = null;
        assertNull(BigDecimals.roundUp(a));
        BigDecimal b = null;
        assertNull(BigDecimals.roundUp(b));
        Double c = null;
        assertNull(BigDecimals.roundUp(c));
    }
}
