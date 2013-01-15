package com.derbysoft.common.exception;

public class HotelNotFoundException extends AbstractRuntimeException {

    public HotelNotFoundException(String message) {
        super(message);
    }

    public HotelNotFoundException(Exception e) {
        super(e);
    }

    public HotelNotFoundException(String message, Exception e) {
        super(message, e);
    }

}
