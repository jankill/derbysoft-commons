package com.derbysoft.common.exception;

public class RatePlanNotFoundException extends AbstractRuntimeException {

    public RatePlanNotFoundException(String message) {
        super(message);
    }

    public RatePlanNotFoundException(Exception e) {
        super(e);
    }

    public RatePlanNotFoundException(String message, Exception e) {
        super(message, e);
    }


}
