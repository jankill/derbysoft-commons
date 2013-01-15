package com.derbysoft.common.exception;

public class ChannelNotFoundException extends AbstractRuntimeException {

    public ChannelNotFoundException(String message) {
        super(message);
    }

    public ChannelNotFoundException(Exception e) {
        super(e);
    }

    public ChannelNotFoundException(String message, Exception e) {
        super(message, e);
    }

}
