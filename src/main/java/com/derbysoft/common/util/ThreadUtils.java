package com.derbysoft.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    private static Log logger = LogFactory.getLog(ThreadUtils.class);

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.warn(ExceptionUtils.toString(e));
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        sleep(unit.toMillis(timeout));
    }
}
