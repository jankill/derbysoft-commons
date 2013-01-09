package com.derbysoft.common.util;

public class FileRuntimeException extends RuntimeException {

    public FileRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public FileRuntimeException(String message) {
        super(message);
    }
}
