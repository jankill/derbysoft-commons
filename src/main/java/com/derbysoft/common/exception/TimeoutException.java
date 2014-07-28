package com.derbysoft.common.exception;

public class TimeoutException extends AbstractRuntimeException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(Exception e) {
        super(e);
    }

    public TimeoutException(String message, Exception e) {
        super(message, e);
    }
}
