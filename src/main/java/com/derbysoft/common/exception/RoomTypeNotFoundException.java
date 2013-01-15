package com.derbysoft.common.exception;

public class RoomTypeNotFoundException extends AbstractRuntimeException {

    public RoomTypeNotFoundException(String message) {
        super(message);
    }

    public RoomTypeNotFoundException(Exception e) {
        super(e);
    }

    public RoomTypeNotFoundException(String message, Exception e) {
        super(message, e);
    }

}
