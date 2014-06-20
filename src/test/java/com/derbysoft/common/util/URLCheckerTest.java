package com.derbysoft.common.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class URLCheckerTest {

    @Test
    public void test() {
        assertFalse(URLChecker.check(" "));
        assertFalse(URLChecker.check(null));
        assertFalse(URLChecker.check("http://"));
        assertFalse(URLChecker.check("https://"));
        assertTrue(URLChecker.check("http://211.144.87.229/sdc/simple/security"));
        assertTrue(URLChecker.check("http://211.144.87.229/sdc/simple/security?dd=dd"));
        assertTrue(URLChecker.check("https://211.144.87.229/sdc/simple/security"));
        assertTrue(URLChecker.check("https://211.144.87.229:8080/sdc/simple/security"));
        assertTrue(URLChecker.check("http://www.google.com"));
        assertTrue(URLChecker.check("http://google.com"));
        assertTrue(URLChecker.check("http://google.com:8080"));
        assertTrue(URLChecker.check("https://www.google.com"));
        assertTrue(URLChecker.check("https://www.google.com.hk/?gws_rd=ssl"));
    }
}
