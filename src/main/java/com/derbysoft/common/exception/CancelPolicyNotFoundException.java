package com.derbysoft.common.exception;

public class CancelPolicyNotFoundException extends AbstractRuntimeException {

    public CancelPolicyNotFoundException(String message) {
        super(message);
    }

    public CancelPolicyNotFoundException(Exception e) {
        super(e);
    }

    public CancelPolicyNotFoundException(String message, Exception e) {
        super(message, e);
    }

}
