package com.derbysoft.common.util.concurrent;

public class ExecuteException extends RuntimeException {

    public ExecuteException(String message, Exception e) {
        super(message, e);
    }

}
