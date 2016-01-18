package com.derbysoft.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.format.DateTimeParseException;

public class DurationsTest {

    @Test
    public void testOfWithoutDefault() {
        Assert.assertEquals(86400000, Durations.of("P1D").toMillis());
        Assert.assertEquals(1000, Durations.of("PT1S").toMillis());
        Assert.assertEquals(1000, Durations.of(1000).toMillis());
        Assert.assertEquals(1000, Durations.of(1000L).toMillis());
        Assert.assertEquals(1000, Durations.of("1000").toMillis());
    }

    @Test
    public void testOfWithDefault() {
        Assert.assertEquals(86400000, Durations.of("P1D", Durations.of(1)).toMillis());
        Assert.assertEquals(1000, Durations.of("PT1S", Durations.of(1)).toMillis());
        Assert.assertEquals(1000, Durations.of(1000, Durations.of(1)).toMillis());

        Assert.assertEquals(1, Durations.of("1D", Durations.of(1)).toMillis());
        Assert.assertEquals(1, Durations.of("P1S", Durations.of(1)).toMillis());
        Assert.assertEquals(1, Durations.of(null, Durations.of(1)).toMillis());
    }

    @Test(expected = DateTimeParseException.class)
    public void testOfWithInvalidFormat() {
        Durations.of("P1S");
    }

    @Test(expected = NullPointerException.class)
    public void testOfWithNullInput() {
        Durations.of(null);
    }
}