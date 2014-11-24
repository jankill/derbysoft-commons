package com.derbysoft.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zhupan
 * @version 1.1
 * @since 2012-5-25
 */
public abstract class Exceptions {

    public static String getExceptionMessage(Throwable e) {
        String message = e.getMessage();
        if (message != null) {
            return message;
        }
        return getStackTrace(e);
    }

    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }

    public static String getErrorMessageWithNestedException(Throwable ex) {
        if (ex instanceof NullPointerException) {
            return getStackTrace(ex);
        }
        Throwable nestedException = ex.getCause();
        return ex.getMessage() + " nested exception is " + nestedException.getClass().getName() + ":" + nestedException.getMessage();
    }

    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        while ((cause = ex.getCause()) != null) {
            ex = cause;
        }
        return ex;
    }

    public static String getStackTrace(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
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
