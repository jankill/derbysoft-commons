package com.derbysoft.common.exception.returnvalue;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public abstract class RegistryBasedException2ReturnValueHandler<ThrowableType extends Throwable>
        extends AbstractException2ReturnValueHandler<ThrowableType> {

    protected Exception2ReturnValueHandlerRegistry registry;

    public RegistryBasedException2ReturnValueHandler(Exception2ReturnValueHandlerRegistry registry) {
        Assert.notNull(registry, "registry required");
        this.registry = registry;
    }

    protected abstract Throwable getTargetException(ThrowableType t);

    protected Object postHandle(Object result, Throwable t, MethodInvocation methodInvocation) {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(ThrowableType t, MethodInvocation methodInvocation) {
        Throwable targetException = getTargetException(t);
        Exception2ReturnValueHandler handler = registry.lookup(targetException.getClass());
        Object result = handler.handle(targetException, methodInvocation);
        result = postHandle(result, targetException, methodInvocation);
        return result;
    }


}
