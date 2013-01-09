package com.derbysoft.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zhupan
 * @version 1.1
 * @since 2012-5-25
 */
public abstract class ThrowableUtils {

    public static String getExceptionMessage(Throwable e) {
        String message = e.getMessage();
        if (message != null) {
            return message;
        }
        return getStackTrace(e);
    }

    public static String getOriginalExceptionMessage(Throwable e) {
        return getExceptionMessage(getOriginalException(e));
    }

    public static String getStackTrace(Throwable throwable) {
        Throwable e = getOriginalException(throwable);
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public static Throwable getOriginalException(Throwable throwable) {
        return getCauseException(getTargetException(throwable));
    }

    public static Throwable getCauseException(Throwable throwable) {
        if (throwable.getCause() == null || throwable == throwable.getCause()) {
            return throwable;
        }
        if (throwable instanceof InvocationTargetException) {
            return getCauseException(((InvocationTargetException) throwable).getTargetException());
        }
        return getCauseException(throwable.getCause());
    }

    public static Throwable getTargetException(Throwable throwable) {
        if (!(throwable instanceof InvocationTargetException)) {
            return throwable;
        }
        return getTargetException(((InvocationTargetException) throwable).getTargetException());
    }
}
