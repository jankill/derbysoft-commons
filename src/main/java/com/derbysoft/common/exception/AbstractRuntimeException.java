package com.derbysoft.common.exception;

public abstract class AbstractRuntimeException extends RuntimeException {

    public AbstractRuntimeException(String message) {
        super(message);
    }

    public AbstractRuntimeException(Exception e) {
        super(e);
    }

    public AbstractRuntimeException(String message, Exception e) {
        super(message, e);
    }

}
