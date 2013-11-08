package com.derbysoft.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupan
 * @version 1.5
 * @since 8/21/13
 */
public class GuestCount implements Comparable {

    private int adult;

    private int child;

    public GuestCount(int adult) {
        this(adult, 0);
    }

    public GuestCount(int adult, int child) {
        this.adult = adult;
        this.child = child;
    }

    public int getAdult() {
        return adult;
    }

    public int getChild() {
        return child;
    }

    public static List<GuestCount> getGuestCounts(int maxOccupancy) {
        List<GuestCount> guestCounts = new ArrayList<GuestCount>();
        for (int i = 1; i <= maxOccupancy; i++) {
            guestCounts.addAll(counts(i, maxOccupancy));
        }
        return guestCounts;
    }

    private static List<GuestCount> counts(int adultCount, int maxOccupancy) {
        List<GuestCount> guestCounts = new ArrayList<GuestCount>();
        int maxChildOcc = maxOccupancy - adultCount;
        for (int j = 0; j <= maxChildOcc; j++) {
            guestCounts.add(new GuestCount(adultCount, j));
        }
        return guestCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuestCount that = (GuestCount) o;
        return adult == that.adult && child == that.child;

    }

    @Override
    public int hashCode() {
        int result = adult;
        result = 31 * result + child;
        return result;
    }

    @Override
    public int compareTo(Object o) {
        GuestCount count = (GuestCount) o;
        if (this.adult == count.getAdult()) {
            return Integer.valueOf(child).compareTo(count.getChild());
        }
        return Integer.valueOf(adult).compareTo(count.getAdult());
    }
}