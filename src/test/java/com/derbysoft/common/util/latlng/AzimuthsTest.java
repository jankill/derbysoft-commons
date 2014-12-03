package com.derbysoft.common.util.latlng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AzimuthsTest {

    @Test
    public void test() {
        LatLng chuansha = new LatLng(31.19374, 121.69757300000003);
        LatLng pudongjichang = new LatLng(31.1443439, 121.80827299999999);
        String direction1 = Azimuths.direction(chuansha, pudongjichang);
        assertEquals("SEE", direction1);
        String direction2 = Azimuths.direction(pudongjichang, chuansha);
        assertEquals("NWW", direction2);

        LatLng shanghai = new LatLng(31.2491620000, 121.4878990000);
        LatLng guangzhou = new LatLng(23.1200490000, 113.3076500000);
        String direction3 = Azimuths.direction(shanghai, guangzhou);
        assertEquals("SW", direction3);
        String direction4 = Azimuths.direction(guangzhou, shanghai);
        assertEquals("NE", direction4);
    }


}
