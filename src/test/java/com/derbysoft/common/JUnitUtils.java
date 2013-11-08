package com.derbysoft.common;

import com.derbysoft.common.util.XStreamFactory;
import com.thoughtworks.xstream.XStream;

import static org.junit.Assert.assertEquals;

public class JUnitUtils {

    public static void assertXMLEquals(Object o1, Object o2) {
        XStream xStream = XStreamFactory.create();
        xStream.setMode(XStream.NO_REFERENCES);
        String xml1 = xStream.toXML(o1);
        String xml2 = xStream.toXML(o2);
        assertEquals(xml1, xml2);
    }

    public static void equals(Object o1, Object o2) {
        XStream xStream = XStreamFactory.create();
        assertEquals(xStream.toXML(o1), xStream.toXML(o2));
    }

}
