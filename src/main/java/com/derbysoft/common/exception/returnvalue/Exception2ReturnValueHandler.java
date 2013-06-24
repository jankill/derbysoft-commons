package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;

public interface Exception2ReturnValueHandler<ThrowableType extends Throwable> {

    Class<ThrowableType> getThrowableType();

    Object handle(ThrowableType t, MethodInvocation methodInvocation);


}
