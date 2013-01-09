package com.derbysoft.common.exception;

public class SystemException extends AbstractRuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Exception e) {
        super(e);
    }

    public SystemException(String message, Exception e) {
        super(message, e);
    }
}
