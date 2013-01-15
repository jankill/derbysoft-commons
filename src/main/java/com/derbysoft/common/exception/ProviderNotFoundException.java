package com.derbysoft.common.exception;

public class ProviderNotFoundException extends AbstractRuntimeException {

    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(Exception e) {
        super(e);
    }

    public ProviderNotFoundException(String message, Exception e) {
        super(message, e);
    }


}
