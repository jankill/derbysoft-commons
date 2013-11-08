package com.derbysoft.common.util;

import com.derbysoft.common.JUnitUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupan
 * @version 1.0
 * @since 8/21/13
 */
public class GuestCountTest {

    @Test
    public void test() {
        JUnitUtils.assertXMLEquals(createExpected(), GuestCount.getGuestCounts(7));
    }

    private List<GuestCount> createExpected() {
        List<GuestCount> counts = new ArrayList<GuestCount>();
        counts.add(new GuestCount(1));
        counts.add(new GuestCount(1, 1));
        counts.add(new GuestCount(1, 2));
        counts.add(new GuestCount(1, 3));
        counts.add(new GuestCount(1, 4));
        counts.add(new GuestCount(1, 5));
        counts.add(new GuestCount(1, 6));
        counts.add(new GuestCount(2));
        counts.add(new GuestCount(2, 1));
        counts.add(new GuestCount(2, 2));
        counts.add(new GuestCount(2, 3));
        counts.add(new GuestCount(2, 4));
        counts.add(new GuestCount(2, 5));
        counts.add(new GuestCount(3));
        counts.add(new GuestCount(3, 1));
        counts.add(new GuestCount(3, 2));
        counts.add(new GuestCount(3, 3));
        counts.add(new GuestCount(3, 4));
        counts.add(new GuestCount(4));
        counts.add(new GuestCount(4, 1));
        counts.add(new GuestCount(4, 2));
        counts.add(new GuestCount(4, 3));
        counts.add(new GuestCount(5));
        counts.add(new GuestCount(5, 1));
        counts.add(new GuestCount(5, 2));
        counts.add(new GuestCount(6));
        counts.add(new GuestCount(6, 1));
        counts.add(new GuestCount(7));
        return counts;
    }

}
