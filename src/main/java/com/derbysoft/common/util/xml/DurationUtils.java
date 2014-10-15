package com.derbysoft.common.util.xml;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public abstract class DurationUtils {
    private static DatatypeFactory datatypeFactory;

    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
        }
    }

    public static Duration ofDays(Integer days) {
        if (days == null) {
            return null;
        }
        return datatypeFactory.newDuration("P" + days + "D");
    }
}
