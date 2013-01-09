package com.derbysoft.common.util;

public class CloneException extends RuntimeException {

    public CloneException(String message, Exception e) {
        super(message, e);
    }

}
