package com.derbysoft.common.util.latlng;

import com.derbysoft.common.util.BigDecimals;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LatLngsTest {

    @Test
    public void testDistance() {
        LatLng chuansha = new LatLng(31.19374, 121.69757300000003);
        LatLng pudongjichang = new LatLng(31.1443439, 121.80827299999999);
        assertEquals(BigDecimals.roundUp(11891.826), BigDecimals.roundUp(LatLngs.distance(chuansha, pudongjichang)));
        assertEquals(BigDecimals.roundUp(11891.826), BigDecimals.roundUp(LatLngs.distance(pudongjichang, chuansha)));

        LatLng shanghai = new LatLng(31.2491620000, 121.4878990000);
        LatLng guangzhou = new LatLng(23.1200490000, 113.3076500000);
        assertEquals(BigDecimals.roundUp(1213607.208), BigDecimals.roundUp(LatLngs.distance(shanghai, guangzhou)));
        assertEquals(BigDecimals.roundUp(1213607.208), BigDecimals.roundUp(LatLngs.distance(guangzhou, shanghai)));
    }

    @Test
    public void testAzimuth() {
        LatLng chuansha = new LatLng(31.19374, 121.69757300000003);
        LatLng pudongjichang = new LatLng(31.1443439, 121.80827299999999);
        int azimuth1 = LatLngs.azimuth(chuansha, pudongjichang);
        int azimuth2 = LatLngs.azimuth(pudongjichang, chuansha);
        assertEquals(117, azimuth1);
        assertEquals(297, azimuth2);
    }

}
