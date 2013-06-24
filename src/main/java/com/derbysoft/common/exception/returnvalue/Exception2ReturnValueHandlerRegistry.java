package com.derbysoft.common.exception.returnvalue;

public interface Exception2ReturnValueHandlerRegistry {

    void registerHandler(Exception2ReturnValueHandler<? extends Throwable> handler);

    Exception2ReturnValueHandler<? extends Throwable> lookup(Class<? extends Throwable> type);


}
