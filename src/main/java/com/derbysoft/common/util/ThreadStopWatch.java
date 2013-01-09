package com.derbysoft.common.util;

import org.apache.commons.lang3.time.StopWatch;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class ThreadStopWatch {

    private static ThreadLocal<StopWatch> stopWatch = new ThreadLocal<StopWatch>();

    public static void start() {
        stopWatch.set(new StopWatch());
        stopWatch.get().start();
    }

    public static void stop() {
        stopWatch.get().stop();
    }

    public static Long getTime() {
        return stopWatch.get().getTime();
    }

    public static Long stopAndGetTime() {
        stop();
        return getTime();
    }

}
