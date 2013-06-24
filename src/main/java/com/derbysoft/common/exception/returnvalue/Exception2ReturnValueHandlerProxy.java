package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;

public class Exception2ReturnValueHandlerProxy extends RegistryBasedException2ReturnValueHandler<Throwable> {

    public Exception2ReturnValueHandlerProxy(Exception2ReturnValueHandlerRegistry registry) {
        super(registry);
    }

    @Override
    protected Throwable getTargetException(Throwable t) {
        return t;
    }

    @Override
    protected Object postHandle(Object result, Throwable t, MethodInvocation methodInvocation) {
        return result;
    }


}
