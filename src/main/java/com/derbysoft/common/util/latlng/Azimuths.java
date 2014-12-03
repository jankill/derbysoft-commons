package com.derbysoft.common.util.latlng;

import java.math.BigDecimal;

public class Azimuths {

    public static String direction(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return null;
        }
        return direction(lat1.doubleValue(), lng1.doubleValue(), lat2.doubleValue(), lng2.doubleValue());
    }

    public static String direction(double lat1, double lng1, double lat2, double lng2) {
        return direction(new LatLng(lat1, lng1), new LatLng(lat2, lng2));
    }

    public static String direction(LatLng from, LatLng to) {
        Direction direction = direction(from.bearingTo(to));
        if (direction == null) {
            return null;
        }
        return direction.name();
    }

    private static Direction direction(int azimuth) {
        if (azimuth > 360 || azimuth < 0) {
            return null;
        } else if ((azimuth <= 12) || (azimuth > 349)) {
            return Direction.N;
        } else if (azimuth > 12 && azimuth <= 34) {
            return Direction.NEN;
        } else if (azimuth > 34 && azimuth <= 57) {
            return Direction.NE;
        } else if (azimuth > 57 && azimuth <= 79) {
            return Direction.NEE;
        } else if (azimuth > 79 && azimuth <= 102) {
            return Direction.E;
        } else if (azimuth > 102 && azimuth <= 124) {
            return Direction.SEE;
        } else if (azimuth > 124 && azimuth <= 147) {
            return Direction.SE;
        } else if (azimuth > 147 && azimuth <= 169) {
            return Direction.SES;
        } else if (azimuth > 169 && azimuth <= 192) {
            return Direction.S;
        } else if (azimuth > 192 && azimuth <= 214) {
            return Direction.SWS;
        } else if (azimuth > 214 && azimuth <= 237) {
            return Direction.SW;
        } else if (azimuth > 237 && azimuth <= 259) {
            return Direction.SWW;
        } else if (azimuth > 259 && azimuth <= 282) {
            return Direction.W;
        } else if (azimuth > 282 && azimuth <= 304) {
            return Direction.NWW;
        } else if (azimuth > 304 && azimuth <= 327) {
            return Direction.NW;
        } else if (azimuth > 327 && azimuth <= 349) {
            return Direction.NWN;
        }
        return null;
    }
}