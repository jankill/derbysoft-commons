package com.derbysoft.common.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class ThreadStopWatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadStopWatch.class);

    private static ThreadLocal<StopWatch> stopWatch = new ThreadLocal<StopWatch>();

    public static void start() {
        try {
            stopWatch.set(new StopWatch());
            stopWatch.get().start();
        } catch (Exception e) {
            LOGGER.warn(ExceptionUtils.toString(e));
        }
    }

    public static void stop() {
        try {
            stopWatch.get().stop();
        } catch (Exception e) {
            LOGGER.warn(ExceptionUtils.toString(e));
        }
    }

    public static Long getTime() {
        try {
            return stopWatch.get().getTime();
        } catch (Exception e) {
            LOGGER.warn(ExceptionUtils.toString(e));
            return 0L;
        }
    }

    public static Long stopAndGetTime() {
        stop();
        return getTime();
    }

}
