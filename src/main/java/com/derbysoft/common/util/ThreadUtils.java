package com.derbysoft.common.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        sleep(unit.toMillis(timeout));
    }
}
