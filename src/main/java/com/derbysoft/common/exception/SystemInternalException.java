package com.derbysoft.common.exception;

public class SystemInternalException extends RuntimeException {

    public SystemInternalException(String message) {
        super(message);
    }

    public SystemInternalException(Exception e) {
        super(e);
    }

    public SystemInternalException(String message, Exception e) {
        super(message, e);
    }
}
