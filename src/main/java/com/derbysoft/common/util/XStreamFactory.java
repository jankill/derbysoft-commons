package com.derbysoft.common.util;

import com.thoughtworks.xstream.XStream;

@Deprecated
public abstract class XStreamFactory {

    public static XStream create() {
        return com.derbysoft.common.factory.XStreamFactory.create();
    }
}
