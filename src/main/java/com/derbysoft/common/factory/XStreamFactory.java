package com.derbysoft.common.factory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;

public abstract class XStreamFactory {

    public static XStream create() {
        XStream xStream = new XStream();
        xStream.registerConverter(new ReflectionConverter(xStream.getMapper(), xStream.getReflectionProvider()), XStream.PRIORITY_LOW + 1);
        xStream.registerConverter(new SerializableConverter(xStream.getMapper(), xStream.getReflectionProvider(), ClassLoader.getSystemClassLoader()), XStream.PRIORITY_LOW + 2);
        return xStream;
    }

    public static XStream createNoReferences() {
        XStream xStream = create();
        xStream.setMode(XStream.NO_REFERENCES);
        return xStream;
    }
}
