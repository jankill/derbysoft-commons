package com.derbysoft.common.exception;

public class ReservationNotFoundException extends AbstractRuntimeException {

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(Exception e) {
        super(e);
    }

    public ReservationNotFoundException(String message, Exception e) {
        super(message, e);
    }


}
