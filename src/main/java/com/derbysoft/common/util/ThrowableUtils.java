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
public class ThrowableUtils {

    public static String getExceptionMessage(Throwable e) {
        String message = e.getMessage();
        if (message != null) {
            return message;
        }
        return getStackTrace(e);
    }

    public static String getOriginalExceptionMessage(Throwable e) {
        return getExceptionMessage(ThrowableUtils.getOriginalException(e));
    }

    public static String getStackTrace(Throwable e) {
        e = getOriginalException(e);
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public static Throwable getOriginalException(Throwable throwable) {
        throwable = getTargetException(throwable);
        return getCauseException(throwable);
    }

    public static Throwable getCauseException(Throwable throwable) {
        if (throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) throwable).getTargetException();
        }
        if (throwable.getCause() == null || throwable == throwable.getCause()) {
            return throwable;
        }
        return getCauseException(throwable.getCause());
    }

    public static Throwable getTargetException(Throwable throwable) {
        if (throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) throwable).getTargetException();
            return getTargetException(throwable);
        }
        return throwable;
    }
}
