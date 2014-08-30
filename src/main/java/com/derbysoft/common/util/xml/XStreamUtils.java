package com.derbysoft.common.util.xml;

import com.derbysoft.common.factory.XStreamFactory;
import com.thoughtworks.xstream.XStream;

public abstract class XStreamUtils {

    private static XStream xStream = XStreamFactory.createNoReferences();

    public static String toXML(Object message) {
        return xStream.toXML(message);
    }

    public static Object fromXML(String xml) {
        return xStream.fromXML(xml);
    }

}
